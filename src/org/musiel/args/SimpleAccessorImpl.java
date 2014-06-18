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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.musiel.args.syntax.Syntax.SyntaxResult;

public class SimpleAccessorImpl implements SimpleAccessor {

	private final SyntaxResult syntaxResult;
	private final Map< String, List< String>> operandMap;

	public SimpleAccessorImpl( final SyntaxResult syntaxResult, final Map< String, List< String>> operandMap) {
		super();
		this.syntaxResult = syntaxResult;
		this.operandMap = operandMap;
	}

	@ Override
	public boolean isOccurred( final String optionName) {
		return !this.getNames( optionName).isEmpty();
	}

	@ Override
	public int getOccurrences( final String optionName) {
		return this.getNames( optionName).size();
	}

	@ Override
	public List< String> getNames( final String optionName) {
		return this.nullToEmptyList( this.syntaxResult.getNames( optionName));
	}

	@ Override
	public String[] getNamesAsArray( final String optionName) {
		return this.toArray( this.getNames( optionName));
	}

	@ Override
	public String getName( final String optionName) {
		return this.getSingle( this.getNames( optionName));
	}

	@ Override
	public List< String> getArguments( final String optionName) {
		return this.nullToEmptyList( this.syntaxResult.getArguments( optionName));
	}

	@ Override
	public String[] getArgumentsAsArray( final String optionName) {
		return this.toArray( this.getArguments( optionName));
	}

	@ Override
	public String getArgument( final String optionName) {
		return this.getSingle( this.getArguments( optionName));
	}

	@ Override
	public List< String> getOperands() {
		return this.syntaxResult.getOperands();
	}

	@ Override
	public String[] getOperandsAsArray() {
		return this.toArray( this.getOperands());
	}

	@ Override
	public String getOperand() {
		return this.getSingle( this.getOperands());
	}

	@ Override
	public List< String> getOperands( final String operandName) {
		return this.nullToEmptyList( this.operandMap.get( operandName));
	}

	@ Override
	public String[] getOperandsAsArray( final String operandName) {
		return this.toArray( this.getOperands( operandName));
	}

	@ Override
	public String getOperand( final String operandName) {
		return this.getSingle( this.getOperands( operandName));
	}

	private String[] toArray( final List< String> list) {
		return list.toArray( new String[ list.size()]);
	}

	private < E>List< E> nullToEmptyList( final List< E> list) {
		return list != null? list: Collections.< E>emptyList();
	}

	private < E>E getSingle( final List< E> list) {
		return list.isEmpty()? null: list.get( 0);
	}
}
