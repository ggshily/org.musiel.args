/*
 * Copyright 2014 Bagana <bagana@musiel.org>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You 
 * may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.musiel.args.printer;

import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.musiel.args.Option;
import org.musiel.args.Parser;

public class GnuMonoTermPrinter implements HelpMessagePrinter {

	public GnuMonoTermPrinter() {
		this( System.out);
	}

	public GnuMonoTermPrinter( final PrintStream out) {
		this( out, 78);
	}

	public GnuMonoTermPrinter( final PrintStream out, final int margin) {
		this( out, margin, 0);
	}

	private final MonoTermPrinter printer;

	public GnuMonoTermPrinter( final PrintStream out, final int margin, final int cursor) {
		this.printer = new MonoTermPrinter( out, margin, cursor);
	}

	private static final String DEFAULT_ARGUMENT_NAME = "ARG";
	private static final int BASE_INDENT = 0;
	private static final int SECTION_INDENT = 2;
	private static final int WRAP_INDENT = 6;
	private static final int OPTION_SPACE = 2;

	@ Override
	public void print( final String commandName, final Parser< ?> parser) {
		this.print( commandName, parser, Locale.getDefault());
	}

	@ Override
	public void print( final String commandName, final Parser< ?> parser, final Locale locale) {
		// USAGE
		final StringBuilder headline = new StringBuilder().append( commandName);
		headline.append( this.constructOptionPart( parser, locale));
		headline.append( this.constructOperandPart( parser));
		this.printer.println( GnuMonoTermPrinter.BASE_INDENT);
		this.printer.print( "USAGE", GnuMonoTermPrinter.BASE_INDENT);
		this.printer.println( GnuMonoTermPrinter.BASE_INDENT);
		this.printer.println( GnuMonoTermPrinter.SECTION_INDENT);
		this.printer.print( headline.toString(), GnuMonoTermPrinter.WRAP_INDENT);
		this.printer.println( GnuMonoTermPrinter.BASE_INDENT);

		// DESCRIPTION
		final String description = parser.getDescription( locale);
		if( description != null) {
			this.printer.println( GnuMonoTermPrinter.BASE_INDENT);
			this.printer.print( "DESCRIPTION", GnuMonoTermPrinter.BASE_INDENT);
			this.printer.println( GnuMonoTermPrinter.BASE_INDENT);
			this.printer.println( GnuMonoTermPrinter.SECTION_INDENT);
			this.printer.print( description, GnuMonoTermPrinter.SECTION_INDENT);
			this.printer.println( GnuMonoTermPrinter.BASE_INDENT);
		}

		// OPTIONS
		if( !parser.getOptions().isEmpty()) {
			final Map< String, String> options = new LinkedHashMap<>();
			int longestHeadWithIndent = -1;
			int longestHeadWithoutIndent = -1;
			boolean longOptionFound = false;
			boolean shortOptionFound = false;
			for( final Option option: parser.getOptions()) {
				final String optionHead = this.constructOptionHead( option, locale);
				final boolean isLong = optionHead.startsWith( "--");
				final int withIndent = isLong? optionHead.length() + 4: optionHead.length();
				final int withoutIndent = optionHead.length();
				longestHeadWithIndent = withIndent > longestHeadWithIndent? withIndent: longestHeadWithIndent;
				longestHeadWithoutIndent = withoutIndent > longestHeadWithoutIndent? withoutIndent: longestHeadWithoutIndent;
				longOptionFound = longOptionFound || isLong;
				shortOptionFound = shortOptionFound || !isLong;
				final String optionDesc = option.getDescription( locale);
				options.put( optionHead, optionDesc == null? "": optionDesc);
			}
			final int longestHead = shortOptionFound? longestHeadWithIndent: longestHeadWithoutIndent;
			final int descIndent = GnuMonoTermPrinter.SECTION_INDENT + longestHead + GnuMonoTermPrinter.OPTION_SPACE;

			this.printer.println( GnuMonoTermPrinter.BASE_INDENT);
			this.printer.print( "OPTIONS", GnuMonoTermPrinter.BASE_INDENT);
			this.printer.println( GnuMonoTermPrinter.BASE_INDENT);
			for( final Entry< String, String> option: options.entrySet()) {
				this.printer.println( GnuMonoTermPrinter.SECTION_INDENT);
				if( shortOptionFound && option.getKey().startsWith( "--"))
					this.printer.print( "    ", GnuMonoTermPrinter.WRAP_INDENT);
				this.printer.print( option.getKey(), GnuMonoTermPrinter.WRAP_INDENT);
				this.printer.forwardTo( descIndent);
				this.printer.print( option.getValue(), descIndent);
			}
			this.printer.println( GnuMonoTermPrinter.BASE_INDENT);
		}
	}

	private String constructOptionPart( final Parser< ?> parser, final Locale locale) {
		final StringBuilder builder = new StringBuilder();
		for( final Option option: parser.getOptions()) {
			builder.append( ' ');
			if( !option.isRequired())
				builder.append( '[');
			builder.append( option.getName());
			if( option.getArgumentPolicy().isAccepted()) {
				if( option.getArgumentPolicy().isRequired())
					builder.append( ' ');
				else if( option.getName().startsWith( "--"))
					builder.append( "[=");
				else
					builder.append( '[');
				final String argumentName = option.getArgumentName( locale);
				builder.append( argumentName != null? argumentName: GnuMonoTermPrinter.DEFAULT_ARGUMENT_NAME);
				if( !option.getArgumentPolicy().isRequired())
					builder.append( ']');
			}
			if( !option.isRequired())
				builder.append( ']');
			if( option.isRepeatable())
				builder.append( "...");
		}
		return builder.toString();
	}

	private Object constructOperandPart( final Parser< ?> parser) {
		final String pattern = parser.getOperandPattern();
		if( pattern == null || "".equals( pattern.trim()))
			return "";
		return " " + pattern;
	}

	private String constructOptionHead( final Option option, final Locale locale) {
		final StringBuilder builder = new StringBuilder();
		boolean lastIsLongOption = false;
		for( final String name: option.getNames()) {
			if( builder.length() > 0)
				builder.append( ", ");
			builder.append( name);
			lastIsLongOption = name.startsWith( "--");
		}
		if( option.getArgumentPolicy().isAccepted()) {
			if( option.getArgumentPolicy().isRequired())
				builder.append( ' ');
			else if( lastIsLongOption)
				builder.append( "[=");
			else
				builder.append( '[');
			final String argumentName = option.getArgumentName( locale);
			builder.append( argumentName != null? argumentName: GnuMonoTermPrinter.DEFAULT_ARGUMENT_NAME);
			if( !option.getArgumentPolicy().isRequired())
				builder.append( ']');
		}
		return builder.toString();
	}
}
