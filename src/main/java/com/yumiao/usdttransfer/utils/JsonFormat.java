package com.yumiao.usdttransfer.utils;

import com.google.protobuf.*;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor.JavaType;
import com.google.protobuf.Descriptors.FieldDescriptor.Type;
import com.google.protobuf.ExtensionRegistry.ExtensionInfo;
import com.google.protobuf.Message.Builder;
import com.google.protobuf.UnknownFieldSet.Field;
import org.apache.commons.lang3.StringUtils;
import org.spongycastle.util.encoders.Hex;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.CharBuffer;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * jsonformat
 *
 * @Autor TrickyZh 2021/1/21
 * @Date 2021-01-21 14:50:55
 */
public class JsonFormat {
	private static final int BUFFER_SIZE = 4096;
	private static final Pattern DIGITS = Pattern.compile("[0-9]", 2);

	public JsonFormat() {
	}

	public static void print(Message message, Appendable output) throws IOException {
		JsonFormat.JsonGenerator generator = new JsonFormat.JsonGenerator(output);
		generator.print("{");
		print(message, generator);
		generator.print("}");
	}

	public static void print(UnknownFieldSet fields, Appendable output) throws IOException {
		JsonFormat.JsonGenerator generator = new JsonFormat.JsonGenerator(output);
		generator.print("{");
		printUnknownFields(fields, generator);
		generator.print("}");
	}

	protected static void print(Message message, JsonFormat.JsonGenerator generator) throws IOException {
		Iterator iter = message.getAllFields().entrySet().iterator();

		while (iter.hasNext()) {
			Entry<FieldDescriptor, Object> field = (Entry) iter.next();
			printField((FieldDescriptor) field.getKey(), field.getValue(), generator);
			if (iter.hasNext()) {
				generator.print(",");
			}
		}

		if (message.getUnknownFields().asMap().size() > 0) {
			generator.print(", ");
		}

		printUnknownFields(message.getUnknownFields(), generator);
	}

	public static String printToString(Message message) {
		try {
			StringBuilder text = new StringBuilder();
			print((Message) message, (Appendable) text);
			return text.toString();
		} catch (IOException var2) {
			throw new RuntimeException("Writing to a StringBuilder threw an IOException (should never happen).", var2);
		}
	}

	public static String printToString(UnknownFieldSet fields) {
		try {
			StringBuilder text = new StringBuilder();
			print((UnknownFieldSet) fields, (Appendable) text);
			return text.toString();
		} catch (IOException var2) {
			throw new RuntimeException("Writing to a StringBuilder threw an IOException (should never happen).", var2);
		}
	}

	public static String printErrorMsg(Exception ex) {
		StringBuilder text = new StringBuilder();
		text.append("{");
		text.append("\"Error\":");
		text.append("\"");
		text.append(ex.getMessage());
		text.append("\"");
		text.append("}");
		return text.toString();
	}

	public static void printField(FieldDescriptor field, Object value, JsonFormat.JsonGenerator generator) throws IOException {
		printSingleField(field, value, generator);
	}

	private static void printSingleField(FieldDescriptor field, Object value, JsonFormat.JsonGenerator generator) throws IOException {
		if (field.isExtension()) {
			generator.print("\"");
			if (field.getContainingType().getOptions().getMessageSetWireFormat() && field.getType() == Type.MESSAGE && field.isOptional() && field.getExtensionScope() == field.getMessageType()) {
				generator.print(field.getMessageType().getFullName());
			} else {
				generator.print(field.getFullName());
			}

			generator.print("\"");
		} else {
			generator.print("\"");
			if (field.getType() == Type.GROUP) {
				generator.print(field.getMessageType().getName());
			} else {
				generator.print(field.getName());
			}

			generator.print("\"");
		}

		if (field.getJavaType() == JavaType.MESSAGE) {
			generator.print(": ");
			generator.indent();
		} else {
			generator.print(": ");
		}

		if (field.isRepeated()) {
			generator.print("[");
			Iterator iter = ((List) value).iterator();

			while (iter.hasNext()) {
				printFieldValue(field, iter.next(), generator);
				if (iter.hasNext()) {
					generator.print(",");
				}
			}

			generator.print("]");
		} else {
			printFieldValue(field, value, generator);
			if (field.getJavaType() == JavaType.MESSAGE) {
				generator.outdent();
			}
		}

	}

	private static void printFieldValue(FieldDescriptor field, Object value, JsonFormat.JsonGenerator generator) throws IOException {
		switch (field.getType()) {
			case INT32:
			case INT64:
			case SINT32:
			case SINT64:
			case SFIXED32:
			case SFIXED64:
			case FLOAT:
			case DOUBLE:
			case BOOL:
				generator.print(value.toString());
				break;
			case UINT32:
			case FIXED32:
				generator.print(unsignedToString((Integer) value));
				break;
			case UINT64:
			case FIXED64:
				generator.print(unsignedToString((Long) value));
				break;
			case STRING:
				generator.print("\"");
				generator.print(escapeText((String) value));
				generator.print("\"");
				break;
			case BYTES:
				generator.print("\"");
				generator.print(escapeBytes((ByteString) value));
				generator.print("\"");
				break;
			case ENUM:
				generator.print("\"");
				generator.print(((EnumValueDescriptor) value).getName());
				generator.print("\"");
				break;
			case MESSAGE:
			case GROUP:
				generator.print("{");
				print((Message) value, generator);
				generator.print("}");
		}

	}

	protected static void printUnknownFields(UnknownFieldSet unknownFields, JsonFormat.JsonGenerator generator) throws IOException {
		boolean firstField = true;
		Iterator var3 = unknownFields.asMap().entrySet().iterator();

		while (var3.hasNext()) {
			Entry<Integer, Field> entry = (Entry) var3.next();
			Field field = (Field) entry.getValue();
			if (firstField) {
				firstField = false;
			} else {
				generator.print(", ");
			}

			generator.print("\"");
			generator.print(((Integer) entry.getKey()).toString());
			generator.print("\"");
			generator.print(": [");
			boolean firstValue = true;

			Iterator var7;
			long value;
			for (var7 = field.getVarintList().iterator(); var7.hasNext(); generator.print(unsignedToString(value))) {
				value = (Long) var7.next();
				if (firstValue) {
					firstValue = false;
				} else {
					generator.print(", ");
				}
			}

			for (var7 = field.getFixed32List().iterator(); var7.hasNext(); generator.print(String.format((Locale) null, "0x%08x", value))) {
				value = (Integer) var7.next();
				if (firstValue) {
					firstValue = false;
				} else {
					generator.print(", ");
				}
			}

			for (var7 = field.getFixed64List().iterator(); var7.hasNext(); generator.print(String.format((Locale) null, "0x%016x", value))) {
				value = (Long) var7.next();
				if (firstValue) {
					firstValue = false;
				} else {
					generator.print(", ");
				}
			}

			var7 = field.getLengthDelimitedList().iterator();

			while (var7.hasNext()) {
				ByteString vv = (ByteString) var7.next();
				if (firstValue) {
					firstValue = false;
				} else {
					generator.print(", ");
				}

				generator.print("\"");
				generator.print(escapeBytes(vv));
				generator.print("\"");
			}

			var7 = field.getGroupList().iterator();

			while (var7.hasNext()) {
				UnknownFieldSet vv = (UnknownFieldSet) var7.next();
				if (firstValue) {
					firstValue = false;
				} else {
					generator.print(", ");
				}

				generator.print("{");
				printUnknownFields(vv, generator);
				generator.print("}");
			}

			generator.print("]");
		}

	}

	private static String unsignedToString(int value) {
		return value >= 0 ? Integer.toString(value) : Long.toString((long) value & 4294967295L);
	}

	private static String unsignedToString(long value) {
		return value >= 0L ? Long.toString(value) : BigInteger.valueOf(value & 9223372036854775807L).setBit(63).toString();
	}

	public static void merge(Readable input, Builder builder) throws IOException {
		merge(input, ExtensionRegistry.getEmptyRegistry(), builder);
	}

	public static void merge(CharSequence input, Builder builder) throws JsonFormat.ParseException {
		merge(input, ExtensionRegistry.getEmptyRegistry(), builder);
	}

	public static void merge(Readable input, ExtensionRegistry extensionRegistry, Builder builder) throws IOException {
		merge((CharSequence) toStringBuilder(input), extensionRegistry, builder);
	}

	public static void merge(CharSequence input, ExtensionRegistry extensionRegistry, Builder builder) throws JsonFormat.ParseException {
		JsonFormat.Tokenizer tokenizer = new JsonFormat.Tokenizer(input);
		tokenizer.consume("{");

		while (!tokenizer.tryConsume("}")) {
			mergeField(tokenizer, extensionRegistry, builder);
		}

		if (!tokenizer.atEnd()) {
			throw tokenizer.parseException("Expecting the end of the stream, but there seems to be more data!  Check the input for a valid JSON format.");
		}
	}

	protected static StringBuilder toStringBuilder(Readable input) throws IOException {
		StringBuilder text = new StringBuilder();
		CharBuffer buffer = CharBuffer.allocate(4096);

		while (true) {
			int n = input.read(buffer);
			if (n == -1) {
				return text;
			}

			buffer.flip();
			text.append(buffer, 0, n);
		}
	}

	protected static void mergeField(JsonFormat.Tokenizer tokenizer, ExtensionRegistry extensionRegistry, Builder builder) throws JsonFormat.ParseException {
		Descriptor type = builder.getDescriptorForType();
		boolean unknown = false;
		String name = tokenizer.consumeIdentifier();
		FieldDescriptor field = type.findFieldByName(name);
		if (field == null) {
			String lowerName = name.toLowerCase(Locale.US);
			field = type.findFieldByName(lowerName);
			if (field != null && field.getType() != Type.GROUP) {
				field = null;
			}
		}

		if (field != null && field.getType() == Type.GROUP && !field.getMessageType().getName().equals(name)) {
			field = null;
		}

		if (field == null && DIGITS.matcher(name).matches()) {
			field = type.findFieldByNumber(Integer.parseInt(name));
			unknown = true;
		}

		ExtensionInfo extension = extensionRegistry.findExtensionByName(name);
		if (extension != null) {
			if (extension.descriptor.getContainingType() != type) {
				throw tokenizer.parseExceptionPreviousToken("Extension \"" + name + "\" does not extend message type \"" + type.getFullName() + "\".");
			}

			field = extension.descriptor;
		}

		if (field == null) {
			handleMissingField(tokenizer, extensionRegistry, builder);
		}

		if (field != null) {
			tokenizer.consume(":");
			boolean array = tokenizer.tryConsume("[");
			if (array) {
				while (!tokenizer.tryConsume("]")) {
					handleValue(tokenizer, extensionRegistry, builder, field, extension, unknown);
					tokenizer.tryConsume(",");
				}
			} else {
				handleValue(tokenizer, extensionRegistry, builder, field, extension, unknown);
			}
		}

		if (tokenizer.tryConsume(",")) {
			mergeField(tokenizer, extensionRegistry, builder);
		}

	}

	private static void handleMissingField(JsonFormat.Tokenizer tokenizer, ExtensionRegistry extensionRegistry, Builder builder) throws JsonFormat.ParseException {
		tokenizer.tryConsume(":");
		if ("{".equals(tokenizer.currentToken())) {
			tokenizer.consume("{");

			do {
				tokenizer.consumeIdentifier();
				handleMissingField(tokenizer, extensionRegistry, builder);
			} while (tokenizer.tryConsume(","));

			tokenizer.consume("}");
		} else if ("[".equals(tokenizer.currentToken())) {
			tokenizer.consume("[");

			do {
				handleMissingField(tokenizer, extensionRegistry, builder);
			} while (tokenizer.tryConsume(","));

			tokenizer.consume("]");
		} else if ("null".equals(tokenizer.currentToken())) {
			tokenizer.consume("null");
		} else if (tokenizer.lookingAtInteger()) {
			tokenizer.consumeInt64();
		} else if (tokenizer.lookingAtBoolean()) {
			tokenizer.consumeBoolean();
		} else {
			tokenizer.consumeString();
		}

	}

	private static void handleValue(JsonFormat.Tokenizer tokenizer, ExtensionRegistry extensionRegistry, Builder builder, FieldDescriptor field, ExtensionInfo extension, boolean unknown) throws JsonFormat.ParseException {
		Object value = null;
		if (field.getJavaType() == JavaType.MESSAGE) {
			value = handleObject(tokenizer, extensionRegistry, builder, field, extension, unknown);
		} else {
			value = handlePrimitive(tokenizer, field);
		}

		if (value != null) {
			if (field.isRepeated()) {
				builder.addRepeatedField(field, value);
			} else {
				builder.setField(field, value);
			}
		}

	}

	private static Object handlePrimitive(JsonFormat.Tokenizer tokenizer, FieldDescriptor field) throws JsonFormat.ParseException {
		Object value = null;
		if ("null".equals(tokenizer.currentToken())) {
			tokenizer.consume("null");
			return value;
		} else {
			switch (field.getType()) {
				case INT32:
				case SINT32:
				case SFIXED32:
					value = tokenizer.consumeInt32();
					break;
				case INT64:
				case SINT64:
				case SFIXED64:
					value = tokenizer.consumeInt64();
					break;
				case FLOAT:
					value = tokenizer.consumeFloat();
					break;
				case DOUBLE:
					value = tokenizer.consumeDouble();
					break;
				case BOOL:
					value = tokenizer.consumeBoolean();
					break;
				case UINT32:
				case FIXED32:
					value = tokenizer.consumeUInt32();
					break;
				case UINT64:
				case FIXED64:
					value = tokenizer.consumeUInt64();
					break;
				case STRING:
					value = tokenizer.consumeString();
					break;
				case BYTES:
					value = tokenizer.consumeByteString();
					break;
				case ENUM:
					EnumDescriptor enumType = field.getEnumType();
					if (tokenizer.lookingAtInteger()) {
						int number = tokenizer.consumeInt32();
						value = enumType.findValueByNumber(number);
						if (value == null) {
							throw tokenizer.parseExceptionPreviousToken("Enum type \"" + enumType.getFullName() + "\" has no value with number " + number + ".");
						}
					} else {
						String id = tokenizer.consumeIdentifier();
						if (StringUtils.isAllLowerCase(id)) {
							char b = id.charAt(0);
							b = (char) (b + 65 - 97);
							String s = id.substring(1);
							id = b + s;
						}

						value = enumType.findValueByName(id);
						if (value == null) {
							throw tokenizer.parseExceptionPreviousToken("Enum type \"" + enumType.getFullName() + "\" has no value named \"" + id + "\".");
						}
					}
					break;
				case MESSAGE:
				case GROUP:
					throw new RuntimeException("Can't get here.");
			}

			return value;
		}
	}

	private static Object handleObject(JsonFormat.Tokenizer tokenizer, ExtensionRegistry extensionRegistry, Builder builder, FieldDescriptor field, ExtensionInfo extension, boolean unknown) throws JsonFormat.ParseException {
		Builder subBuilder;
		if (extension == null) {
			subBuilder = builder.newBuilderForField(field);
		} else {
			subBuilder = extension.defaultInstance.newBuilderForType();
		}

		if (unknown) {
			ByteString data = tokenizer.consumeByteString();

			try {
				subBuilder.mergeFrom(data);
				return subBuilder.build();
			} catch (InvalidProtocolBufferException var9) {
				throw tokenizer.parseException("Failed to build " + field.getFullName() + " from " + data);
			}
		} else {
			tokenizer.consume("{");
			String endToken = "}";

			while (!tokenizer.tryConsume(endToken)) {
				if (tokenizer.atEnd()) {
					throw tokenizer.parseException("Expected \"" + endToken + "\".");
				}

				mergeField(tokenizer, extensionRegistry, subBuilder);
				if (tokenizer.tryConsume(",")) {
				}
			}

			return subBuilder.build();
		}
	}

	static String escapeBytes(ByteString input) {
		return Hex.toHexString(input.toByteArray());
	}

	static String unicodeEscaped(char ch) {
		if (ch < 16) {
			return "\\u000" + Integer.toHexString(ch);
		} else if (ch < 256) {
			return "\\u00" + Integer.toHexString(ch);
		} else {
			return ch < 4096 ? "\\u0" + Integer.toHexString(ch) : "\\u" + Integer.toHexString(ch);
		}
	}

	static ByteString unescapeBytes(CharSequence input) throws JsonFormat.InvalidEscapeSequence {
		try {
			return ByteString.copyFrom(Hex.decode(input.toString()));
		} catch (Exception var2) {
			throw new JsonFormat.InvalidEscapeSequence("invalidate hex String");
		}
	}

	static String escapeText(String input) {
		StringBuilder builder = new StringBuilder(input.length());
		CharacterIterator iter = new StringCharacterIterator(input);

		for (char c = iter.first(); c != '\uffff'; c = iter.next()) {
			switch (c) {
				case '\b':
					builder.append("\\b");
					break;
				case '\t':
					builder.append("\\t");
					break;
				case '\n':
					builder.append("\\n");
					break;
				case '\f':
					builder.append("\\f");
					break;
				case '\r':
					builder.append("\\r");
					break;
				case '"':
					builder.append("\\\"");
					break;
				case '\\':
					builder.append("\\\\");
					break;
				default:
					if (c >= 0 && c <= 31) {
						appendEscapedUnicode(builder, c);
					} else if (Character.isHighSurrogate(c)) {
						appendEscapedUnicode(builder, c);
						c = iter.next();
						if (c == '\uffff') {
							throw new IllegalArgumentException("invalid unicode string: unexpected high surrogate pair value without corresponding low value.");
						}

						appendEscapedUnicode(builder, c);
					} else {
						builder.append(c);
					}
			}
		}

		return builder.toString();
	}

	static void appendEscapedUnicode(StringBuilder builder, char ch) {
		String prefix = "\\u";
		if (ch < 16) {
			prefix = "\\u000";
		} else if (ch < 256) {
			prefix = "\\u00";
		} else if (ch < 4096) {
			prefix = "\\u0";
		}

		builder.append(prefix).append(Integer.toHexString(ch));
	}

	static String unescapeText(String input) throws JsonFormat.InvalidEscapeSequence {
		StringBuilder builder = new StringBuilder();
		char[] array = input.toCharArray();

		for (int i = 0; i < array.length; ++i) {
			char c = array[i];
			if (c == '\\') {
				if (i + 1 >= array.length) {
					throw new JsonFormat.InvalidEscapeSequence("Invalid escape sequence: '\\' at end of string.");
				}

				++i;
				c = array[i];
				switch (c) {
					case '"':
						builder.append('"');
						break;
					case '\'':
						builder.append('\'');
						break;
					case '\\':
						builder.append('\\');
						break;
					case 'b':
						builder.append('\b');
						break;
					case 'f':
						builder.append('\f');
						break;
					case 'n':
						builder.append('\n');
						break;
					case 'r':
						builder.append('\r');
						break;
					case 't':
						builder.append('\t');
						break;
					case 'u':
						if (i + 4 >= array.length) {
							throw new JsonFormat.InvalidEscapeSequence("Invalid escape sequence: '\\u' at end of string.");
						}

						++i;
						int code = Integer.parseInt(new String(array, i, 4), 16);
						builder.append((char) code);
						i += 3;
						break;
					default:
						throw new JsonFormat.InvalidEscapeSequence("Invalid escape sequence: '\\" + c + "'");
				}
			} else {
				builder.append(c);
			}
		}

		return builder.toString();
	}

	private static boolean isOctal(char c) {
		return '0' <= c && c <= '7';
	}

	private static boolean isHex(char c) {
		return '0' <= c && c <= '9' || 'a' <= c && c <= 'f' || 'A' <= c && c <= 'F';
	}

	private static int digitValue(char c) {
		if ('0' <= c && c <= '9') {
			return c - 48;
		} else {
			return 'a' <= c && c <= 'z' ? c - 97 + 10 : c - 65 + 10;
		}
	}

	static int parseInt32(String text) throws NumberFormatException {
		return (int) parseInteger(text, true, false);
	}

	static int parseUInt32(String text) throws NumberFormatException {
		return (int) parseInteger(text, false, false);
	}

	static long parseInt64(String text) throws NumberFormatException {
		return parseInteger(text, true, true);
	}

	static long parseUInt64(String text) throws NumberFormatException {
		return parseInteger(text, false, true);
	}

	private static long parseInteger(String text, boolean isSigned, boolean isLong) throws NumberFormatException {
		int pos = 0;
		boolean negative = false;
		if (text.startsWith("-", pos)) {
			if (!isSigned) {
				throw new NumberFormatException("Number must be positive: " + text);
			}

			++pos;
			negative = true;
		}

		int radix = 10;
		if (text.startsWith("0x", pos)) {
			pos += 2;
			radix = 16;
		} else if (text.startsWith("0", pos)) {
			radix = 8;
		}

		String numberText = text.substring(pos);
		long result = 0L;
		if (numberText.length() < 16) {
			result = Long.parseLong(numberText, radix);
			if (negative) {
				result = -result;
			}

			if (!isLong) {
				if (isSigned) {
					if (result > 2147483647L || result < -2147483648L) {
						throw new NumberFormatException("Number out of range for 32-bit signed integer: " + text);
					}
				} else if (result >= 4294967296L || result < 0L) {
					throw new NumberFormatException("Number out of range for 32-bit unsigned integer: " + text);
				}
			}
		} else {
			BigInteger bigValue = new BigInteger(numberText, radix);
			if (negative) {
				bigValue = bigValue.negate();
			}

			if (!isLong) {
				if (isSigned) {
					if (bigValue.bitLength() > 31) {
						throw new NumberFormatException("Number out of range for 32-bit signed integer: " + text);
					}
				} else if (bigValue.bitLength() > 32) {
					throw new NumberFormatException("Number out of range for 32-bit unsigned integer: " + text);
				}
			} else if (isSigned) {
				if (bigValue.bitLength() > 63) {
					throw new NumberFormatException("Number out of range for 64-bit signed integer: " + text);
				}
			} else if (bigValue.bitLength() > 64) {
				throw new NumberFormatException("Number out of range for 64-bit unsigned integer: " + text);
			}

			result = bigValue.longValueExact();
		}

		return result;
	}

	static class InvalidEscapeSequence extends IOException {
		private static final long serialVersionUID = 1L;

		public InvalidEscapeSequence(String description) {
			super(description);
		}
	}

	public static class ParseException extends IOException {
		private static final long serialVersionUID = 1L;

		public ParseException(String message) {
			super(message);
		}
	}

	protected static class Tokenizer {
		private static final Pattern WHITESPACE = Pattern.compile("(\\s|(#.*$))++", 8);
		private static final Pattern TOKEN = Pattern.compile("[a-zA-Z_][0-9a-zA-Z_+-]*+|[.]?[0-9+-][0-9a-zA-Z_.+-]*+|\"([^\"\n\\\\]|\\\\.)*+(\"|\\\\?$)|'([^'\n\\\\]|\\\\.)*+('|\\\\?$)", 8);
		private static final Pattern DOUBLE_INFINITY = Pattern.compile("-?inf(inity)?", 2);
		private static final Pattern FLOAT_INFINITY = Pattern.compile("-?inf(inity)?f?", 2);
		private static final Pattern FLOAT_NAN = Pattern.compile("nanf?", 2);
		private final CharSequence text;
		private final Matcher matcher;
		private String currentToken;
		private int pos = 0;
		private int line = 0;
		private int column = 0;
		private int previousLine = 0;
		private int previousColumn = 0;

		public Tokenizer(CharSequence text) {
			this.text = text;
			this.matcher = WHITESPACE.matcher(text);
			this.skipWhitespace();
			this.nextToken();
		}

		public boolean atEnd() {
			return this.currentToken.length() == 0;
		}

		public void nextToken() {
			this.previousLine = this.line;

			for (this.previousColumn = this.column; this.pos < this.matcher.regionStart(); ++this.pos) {
				if (this.text.charAt(this.pos) == '\n') {
					++this.line;
					this.column = 0;
				} else {
					++this.column;
				}
			}

			if (this.matcher.regionStart() == this.matcher.regionEnd()) {
				this.currentToken = "";
			} else {
				this.matcher.usePattern(TOKEN);
				if (this.matcher.lookingAt()) {
					this.currentToken = this.matcher.group();
					this.matcher.region(this.matcher.end(), this.matcher.regionEnd());
				} else {
					this.currentToken = String.valueOf(this.text.charAt(this.pos));
					this.matcher.region(this.pos + 1, this.matcher.regionEnd());
				}

				this.skipWhitespace();
			}

		}

		private void skipWhitespace() {
			this.matcher.usePattern(WHITESPACE);
			if (this.matcher.lookingAt()) {
				this.matcher.region(this.matcher.end(), this.matcher.regionEnd());
			}

		}

		public boolean tryConsume(String token) {
			if (this.currentToken.equals(token)) {
				this.nextToken();
				return true;
			} else {
				return false;
			}
		}

		public void consume(String token) throws JsonFormat.ParseException {
			if (!this.tryConsume(token)) {
				throw this.parseException("Expected \"" + token + "\".");
			}
		}

		public boolean lookingAtInteger() {
			if (this.currentToken.length() == 0) {
				return false;
			} else {
				char c = this.currentToken.charAt(0);
				return '0' <= c && c <= '9' || c == '-' || c == '+';
			}
		}

		public boolean lookingAtBoolean() {
			if (this.currentToken.length() == 0) {
				return false;
			} else {
				return "true".equals(this.currentToken) || "false".equals(this.currentToken);
			}
		}

		static ByteString unescapeBytes(CharSequence input) throws JsonFormat.InvalidEscapeSequence {
			try {
				return ByteString.copyFrom(Hex.decode(input.toString()));
			} catch (Exception var2) {
				throw new JsonFormat.InvalidEscapeSequence("INVALID hex String");
			}
		}

		public String currentToken() {
			return this.currentToken;
		}

		public String consumeIdentifier() throws JsonFormat.ParseException {
			for (int i = 0; i < this.currentToken.length(); ++i) {
				char c = this.currentToken.charAt(i);
				if (('a' > c || c > 'z') && ('A' > c || c > 'Z') && ('0' > c || c > '9') && c != '_' && c != '.' && c != '"') {
					throw this.parseException("Expected identifier. -" + c);
				}
			}

			String result = this.currentToken;
			result = result.replaceAll("\"|'", "");
			this.nextToken();
			return result;
		}

		public int consumeInt32() throws JsonFormat.ParseException {
			try {
				int result = JsonFormat.parseInt32(this.currentToken);
				this.nextToken();
				return result;
			} catch (NumberFormatException var2) {
				throw this.integerParseException(var2);
			}
		}

		public int consumeUInt32() throws JsonFormat.ParseException {
			try {
				int result = JsonFormat.parseUInt32(this.currentToken);
				this.nextToken();
				return result;
			} catch (NumberFormatException var2) {
				throw this.integerParseException(var2);
			}
		}

		public long consumeInt64() throws JsonFormat.ParseException {
			try {
				long result = JsonFormat.parseInt64(this.currentToken);
				this.nextToken();
				return result;
			} catch (NumberFormatException var3) {
				throw this.integerParseException(var3);
			}
		}

		public long consumeUInt64() throws JsonFormat.ParseException {
			try {
				long result = JsonFormat.parseUInt64(this.currentToken);
				this.nextToken();
				return result;
			} catch (NumberFormatException var3) {
				throw this.integerParseException(var3);
			}
		}

		public double consumeDouble() throws JsonFormat.ParseException {
			if (DOUBLE_INFINITY.matcher(this.currentToken).matches()) {
				boolean negative = this.currentToken.startsWith("-");
				this.nextToken();
				return negative ? -1.0D / 0.0 : 1.0D / 0.0;
			} else if (this.currentToken.equalsIgnoreCase("nan")) {
				this.nextToken();
				return 0.0D / 0.0;
			} else {
				try {
					double result = Double.parseDouble(this.currentToken);
					this.nextToken();
					return result;
				} catch (NumberFormatException var3) {
					throw this.floatParseException(var3);
				}
			}
		}

		public float consumeFloat() throws JsonFormat.ParseException {
			if (FLOAT_INFINITY.matcher(this.currentToken).matches()) {
				boolean negative = this.currentToken.startsWith("-");
				this.nextToken();
				return negative ? -1.0F : 1.0F;
			} else if (FLOAT_NAN.matcher(this.currentToken).matches()) {
				this.nextToken();
				return 0.0F;
			} else {
				try {
					float result = Float.parseFloat(this.currentToken);
					this.nextToken();
					return result;
				} catch (NumberFormatException var2) {
					throw this.floatParseException(var2);
				}
			}
		}

		public boolean consumeBoolean() throws JsonFormat.ParseException {
			if (this.currentToken.equals("true")) {
				this.nextToken();
				return true;
			} else if (this.currentToken.equals("false")) {
				this.nextToken();
				return false;
			} else {
				throw this.parseException("Expected \"true\" or \"false\".");
			}
		}

		public String consumeString() throws JsonFormat.ParseException {
			char quote = this.currentToken.length() > 0 ? this.currentToken.charAt(0) : 0;
			if (quote != '"' && quote != '\'') {
				throw this.parseException("Expected string.");
			} else if (this.currentToken.length() >= 2 && this.currentToken.charAt(this.currentToken.length() - 1) == quote) {
				try {
					String escaped = this.currentToken.substring(1, this.currentToken.length() - 1);
					String result = JsonFormat.unescapeText(escaped);
					this.nextToken();
					return result;
				} catch (JsonFormat.InvalidEscapeSequence var4) {
					throw this.parseException(var4.getMessage());
				}
			} else {
				throw this.parseException("String missing ending quote.");
			}
		}

		public ByteString consumeByteString() throws JsonFormat.ParseException {
			char quote = this.currentToken.length() > 0 ? this.currentToken.charAt(0) : 0;
			if (quote != '"' && quote != '\'') {
				throw this.parseException("Expected string.");
			} else if (this.currentToken.length() >= 2 && this.currentToken.charAt(this.currentToken.length() - 1) == quote) {
				try {
					String escaped = this.currentToken.substring(1, this.currentToken.length() - 1);
					ByteString result = unescapeBytes(escaped);
					this.nextToken();
					return result;
				} catch (JsonFormat.InvalidEscapeSequence var4) {
					throw this.parseException(var4.getMessage());
				}
			} else {
				throw this.parseException("String missing ending quote.");
			}
		}

		public JsonFormat.ParseException parseException(String description) {
			return new JsonFormat.ParseException(this.line + 1 + ":" + (this.column + 1) + ": " + description);
		}

		public JsonFormat.ParseException parseExceptionPreviousToken(String description) {
			return new JsonFormat.ParseException(this.previousLine + 1 + ":" + (this.previousColumn + 1) + ": " + description);
		}

		private JsonFormat.ParseException integerParseException(NumberFormatException e) {
			return this.parseException("Couldn't parse integer: " + e.getMessage());
		}

		private JsonFormat.ParseException floatParseException(NumberFormatException e) {
			return this.parseException("Couldn't parse number: " + e.getMessage());
		}
	}

	protected static class JsonGenerator {
		private Appendable output;
		private boolean atStartOfLine = true;
		private StringBuilder indent = new StringBuilder();

		public JsonGenerator(Appendable output) {
			this.output = output;
		}

		public void indent() {
			this.indent.append("  ");
		}

		public void outdent() {
			int length = this.indent.length();
			if (length == 0) {
				throw new IllegalArgumentException(" Outdent() without matching Indent().");
			} else {
				this.indent.delete(length - 2, length);
			}
		}

		public void print(CharSequence text) throws IOException {
			int size = text.length();
			int pos = 0;

			for (int i = 0; i < size; ++i) {
				if (text.charAt(i) == '\n') {
					this.write(text.subSequence(pos, size), i - pos + 1);
					pos = i + 1;
					this.atStartOfLine = true;
				}
			}

			this.write(text.subSequence(pos, size), size - pos);
		}

		private void write(CharSequence data, int size) throws IOException {
			if (size != 0) {
				if (this.atStartOfLine) {
					this.atStartOfLine = false;
					this.output.append(this.indent);
				}

				this.output.append(data);
			}
		}
	}
}
