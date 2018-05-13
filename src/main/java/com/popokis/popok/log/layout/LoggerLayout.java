package com.popokis.popok.log.layout;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.popokis.popok.log.common.LoggerUtils;
import com.popokis.popok.log.context.LoggerContextOperation;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.apache.logging.log4j.util.Strings;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Plugin(name = "LoggerLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public final class LoggerLayout extends AbstractStringLayout {

  private static final String CONTENT_TYPE = "application/json";
  private static final String DEFAULT_HEADER = "[";
  private static final String DEFAULT_FOOTER = "]";
  private static final String DEFAULT_EOL = "\r\n";
  private static final String COMPACT_EOL = Strings.EMPTY;


  private final KeyValuePair[] additionalFields;
  private final boolean locationInfo;
  private final boolean properties;
  private final boolean complete;
  private final String eol;
  private final StrSubstitutor strSubstitutor;

  protected LoggerLayout(final Configuration config, final boolean locationInfo,
                         final boolean properties, final boolean complete, final boolean eventEol,
                         final String headerPattern, final String footerPattern, final Charset charset,
                         final KeyValuePair[] additionalFields) {
    super(config, charset, //

        PatternLayout.newSerializerBuilder() //
            .setConfiguration(config).setReplace(null).setPattern(headerPattern) //
            .setDefaultPattern(DEFAULT_HEADER) //
            .setPatternSelector(null).setAlwaysWriteExceptions(false).setNoConsoleNoAnsi(false) //
            .build(), //

        PatternLayout.newSerializerBuilder() //
            .setConfiguration(config).setReplace(null).setPattern(footerPattern) //
            .setDefaultPattern(DEFAULT_FOOTER) //
            .setPatternSelector(null).setAlwaysWriteExceptions(false).setNoConsoleNoAnsi(false) //
            .build());

    this.locationInfo = locationInfo;
    this.properties = properties;
    this.complete = complete;
    this.additionalFields = additionalFields;

    this.strSubstitutor = configuration.getStrSubstitutor();

    this.eol = !eventEol ? COMPACT_EOL : DEFAULT_EOL;
  }

  @Override
  public Map<String, String> getContentFormat() {
    return super.getContentFormat();
  }

  @Override
  public String getContentType() {
    return CONTENT_TYPE + "; charset=" + this.getCharset();
  }

  @Override
  public byte[] getHeader() {
    if (!this.complete) {
      return null;
    }
    final StringBuilder buf = new StringBuilder();
    final String str = serializeToString(getHeaderSerializer());
    if (str != null) {
      buf.append(str);
    }
    buf.append(this.eol);
    return getBytes(buf.toString());
  }

  @Override
  public byte[] getFooter() {
    if (!this.complete) {
      return null;
    }
    final StringBuilder buf = new StringBuilder();
    buf.append(this.eol);
    final String str = serializeToString(getFooterSerializer());
    if (str != null) {
      buf.append(str);
    }
    buf.append(this.eol);
    return getBytes(buf.toString());
  }

  @Override
  public String toSerializable(final LogEvent event) {
    try {
      return format(convertMutableToLog4jEvent(event));
    } finally {
      markEvent();
    }
  }

  /**
   * (LOGBACK LAYOUT NEEDED)
   * @param event
   * @return
   */
  private static LogEvent convertMutableToLog4jEvent(final LogEvent event) {
    // TODO (from JsonLayout): Need to set up the same filters for MutableLogEvent but don't know how...
    return ((event instanceof MutableLogEvent) ? ((MutableLogEvent) event).createMemento() : event);
  }

  /**
   * Get the minimum information any log must contain
   *
   * @param event
   * @param mapper
   * @return
   */
  private ObjectNode getBasicInfoLog(final LogEvent event, ObjectMapper mapper) {
    ObjectNode logBody = mapper.createObjectNode();

    logBody.put("level", event.getLevel().toString());
    logBody.put("logger", event.getLoggerName());

    this.getAdditionalFields().forEach(kv -> logBody.put(kv.getKey(), kv.getValue()));

    logBody.put("msg", event.getMessage().getFormattedMessage());

    return logBody;
  }

  /**
   * Get additional fields (usually present in the log4j2.xml config file as
   * <KeyValuePair />
   *
   * @return
   */
  private List<KeyValuePair> getAdditionalFields() {
    return Arrays
        .stream(this.additionalFields)
        .map((e) -> new KeyValuePair(e.getKey(), strSubstitutor.replace(e.getValue())))
        .collect(Collectors.toList());
  }

  /**
   * Get Caller info BEWARE this operation has an important perfomance cost, use wisely.
   * @param event
   * @param mapper
   * @return
   */
  private ObjectNode getLocationInfo(final LogEvent event, ObjectMapper mapper) {
    ObjectNode locationBody = mapper.createObjectNode();

    final StackTraceElement source = event.getSource();
    if(source != null) {
      locationBody.put("class", source.getClassName());
      locationBody.put("method", source.getMethodName());
      locationBody.put("file", source.getFileName());
      locationBody.put("line", String.valueOf(source.getLineNumber()));
    } else {
      locationBody.put("class", "information not available");
      locationBody.put("method", "information not available");
      locationBody.put("file", "information not available");
      locationBody.put("line", "information not available");
    }

    return locationBody;
  }

  /**
   * If we have an exception in our log, we get all the relevant information as an
   * independent node
   * @param event
   * @param mapper
   * @return
   */
  private ObjectNode getExceptionLog(final LogEvent event, ObjectMapper mapper) {
    ObjectNode exceptionBody = mapper.createObjectNode();

    if (event.getThrownProxy() != null) {
      final ThrowableProxy throwableInfo = event.getThrownProxy();
      final Throwable t = throwableInfo.getThrowable();

      exceptionBody.put("class", t.getClass().getCanonicalName());

      final String exMsg = t.getMessage();
      if (exMsg != null) exceptionBody.put("cause", exMsg);

      // TODO: Change pure string to complex list/maps of stacktraces?
      final String stackTrace = throwableInfo.getExtendedStackTraceAsString("");
      if (stackTrace != null) exceptionBody.put("stacktrace", stackTrace);
    }

    return exceptionBody;
  }

  /**
   * Get a list of values from the NCD context
   * @param event
   * @param mapper
   * @return
   */
  private ObjectNode getContextStackList(final LogEvent event, ObjectMapper mapper) {
    ObjectNode stackList = mapper.createObjectNode();
    ArrayNode extraValues = mapper.createArrayNode();

    LoggerContextOperation.getNDCList(LoggerContextOperation.filterNDC(event.getContextData().toMap()))
        .forEach(extraValues::add);

    if(extraValues.size() > 0) stackList.set("extraValues", extraValues);
    return stackList;
  }

  /**
   * Get all mapped values from MCD context
   * @param event
   * @param mapper
   * @return
   */
  private ObjectNode getContextMappedList(final LogEvent event, ObjectMapper mapper) {
    ObjectNode mappedNode = mapper.createObjectNode();

    LoggerContextOperation.filterMDC(event.getContextData().toMap())
        .forEach((key, value) -> {
          Optional<JsonNode> jsonNode = LoggerUtils.parseJSON(value);
          if(jsonNode.isPresent()) {
            mappedNode.set(key, jsonNode.get());
          } else {
            mappedNode.put(key, value);
          }
        });

    return mappedNode;
  }

  /**
   * Assembly the log data structure and then use Jackson to have a JSON output
   * @param event
   * @return
   */
  private String formatWithJackson(final LogEvent event) {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode basicInfoLog = this.getBasicInfoLog(event, mapper);
    ObjectNode exceptionLog = this.getExceptionLog(event, mapper);

    // we add extra information if present/needed
    if(exceptionLog.hasNonNull("class")) basicInfoLog.set("exception", exceptionLog);
    if(this.locationInfo) basicInfoLog.set("caller", this.getLocationInfo(event, mapper));

    if(this.properties) {
      basicInfoLog.setAll(this.getContextMappedList(event, mapper)); // MCD
      basicInfoLog.setAll(this.getContextStackList(event, mapper)); // NCD
    }

    return basicInfoLog.toString() + this.eol;
  }

  private String format(final LogEvent event) {
    return this.formatWithJackson(event);
  }

  @PluginFactory
  public static LoggerLayout createLayout( //
                                           @PluginConfiguration final Configuration config, //
                                           @PluginAttribute(value = "locationInfo", defaultBoolean = false) final boolean locationInfo, //
                                           @PluginAttribute(value = "properties", defaultBoolean = true) final boolean properties, //
                                           @PluginAttribute(value = "complete", defaultBoolean = false) final boolean complete, //
                                           @PluginAttribute(value = "eventEol", defaultBoolean = true) final boolean eventEol, //
                                           @PluginAttribute(value = "header", defaultString = DEFAULT_HEADER) final String headerPattern, //
                                           @PluginAttribute(value = "footer", defaultString = DEFAULT_FOOTER) final String footerPattern, //
                                           @PluginAttribute(value = "charset", defaultString = "UTF8") final Charset charset, //
                                           @PluginElement("AdditionalField") final KeyValuePair[] additionalFields) {
    return new LoggerLayout(config, locationInfo, properties, complete, eventEol, headerPattern,
        footerPattern, charset, additionalFields);
  }
}