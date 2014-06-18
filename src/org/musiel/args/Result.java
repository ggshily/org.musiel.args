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

/**
 * The result of a {@link Parser#parse(String...)} invocation.
 * 
 * @param <ACCESSOR>
 * 
 * @author Bagana
 */
public interface Result< ACCESSOR> {

	/**
	 * Returns the {@link ArgumentException}s encountered during the parsing. An empty collection is returned if no error is detected.
	 * 
	 * @return
	 */
	public Collection< ? extends ArgumentException> getErrors();

	/**
	 * Searches {@link #getErrors()} for {@link ArgumentException}s of the specified types and throws them if found. Otherwise, returns
	 * current object.
	 * 
	 * @param exceptionTypes
	 * @return
	 * @throws ArgumentExceptions
	 */
	public Result< ACCESSOR> check( Collection< Class< ? extends ArgumentException>> exceptionTypes) throws ArgumentExceptions;

	/**
	 * Searches {@link #getErrors()} for {@link ArgumentException} of the specified type and throws them if found. Otherwise, returns
	 * current object.
	 * 
	 * @param exceptionType
	 * @return
	 * @throws ArgumentExceptions
	 */
	public Result< ACCESSOR> check( Class< ? extends ArgumentException> exceptionType) throws ArgumentExceptions;

	/**
	 * Throws all user errors in a {@link ArgumentExceptions} wrapper, or returns an accessor if no error is found. The accessor returned
	 * in this way guarantees to comply every requirements, constraints, and patterns of the options and operands.
	 * 
	 * @return
	 * @throws ArgumentExceptions
	 */
	public ACCESSOR check() throws ArgumentExceptions;

	/**
	 * Returns the accessor without checking the constraints. The returned accessor may carry incorrect and/or inconsistent data. A
	 * {@link Parser} implementation can elaborate more precisely on which methods of the return value are reliable or unreliable in this
	 * case.
	 * 
	 * @return
	 */
	public ACCESSOR getAccessor();
}
