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
package org.musiel.args;

import java.util.Collection;
import java.util.Locale;

import org.musiel.args.reflect.ReflectParser;

/**
 * A command line argument parser.
 * 
 * @param <RESULT>
 * 
 * @see DefaultParser
 * @see ReflectParser
 * @author Bagana
 */
public interface Parser< RESULT extends Result< ?>> {

	/**
	 * Returns a collection of defined options.
	 * 
	 * @return
	 */
	public Collection< ? extends Option> getOptions();

	/**
	 * Returns the {@link Option} defined with the specified name, or <code>null</code> if no such option exists.
	 * 
	 * @param optionName
	 * @return
	 */
	public Option getOption( String optionName);

	/**
	 * Returns a collection of all operand names, or <code>null</code> if no operand pattern is defined. If it is not <code>null</code>,
	 * all elements appear in the operand pattern ({@link #getOperandPattern()}.
	 * 
	 * @return
	 */
	public Collection< String> getOperandNames();

	/**
	 * Returns the operand pattern, or <code>null</code> if no operand pattern is defined.
	 * 
	 * @return
	 */
	public String getOperandPattern();

	/**
	 * Parses an array of command line arguments and returns the result. The parsing continues on input errors, and all the errors can be
	 * obtained afterward, via {@link Result#getErrors()}.
	 * 
	 * @param args
	 * @return
	 */
	public RESULT parse( final String... args);

	/**
	 * Equivalent to calling {@link #parse(String...)} with a sub-array of {@code args} from index {@code offset} (inclusive) to the end of
	 * the array.
	 * 
	 * @param args
	 * @param offset
	 * @return
	 */
	public RESULT parse( final String[] args, final int offset);

	/**
	 * Equivalent to calling {@link #parse(String...)} with a sub-array of {@code args} from index {@code offset} (inclusive) to
	 * {@code offset + length} (exclusive).
	 * 
	 * @param args
	 * @param offset
	 * @param length
	 * @return
	 */
	public RESULT parse( final String[] args, final int offset, final int length);

	/**
	 * Returns a human readable description of the command line interface (typically used in help message printing), or <code>null</code>
	 * if no information is available.
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * Returns a human readable description of the command line interface in the specified locale (typically used in help message
	 * printing), or <code>null</code> if no information is available.
	 * 
	 * @param locale Must not be null
	 * @return
	 */
	public String getDescription( Locale locale);

	/**
	 * Returns a human readable description of an operand (typically used in help message printing), or <code>null</code> if no information
	 * is available, or the {@code operandName} does not exist.
	 * 
	 * @param operandName
	 * @return
	 */
	public String getOperandDescription( String operandName);

	/**
	 * Returns a human readable description of an operand in the specified locale (typically used in help message printing), or
	 * <code>null</code> if no information is available, or the {@code operandName} does not exist.
	 * 
	 * @param operandName
	 * @param locale
	 * @return
	 */
	public String getOperandDescription( String operandName, Locale locale);
}
