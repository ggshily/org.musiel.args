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
package org.musiel.args.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.musiel.args.AbstractParser;
import org.musiel.args.AbstractResult;
import org.musiel.args.ArgumentException;
import org.musiel.args.ArgumentPolicy;
import org.musiel.args.Option;
import org.musiel.args.Result;
import org.musiel.args.SimpleAccessor;
import org.musiel.args.SimpleAccessorImpl;
import org.musiel.args.syntax.GnuSyntax;
import org.musiel.args.syntax.Syntax;
import org.musiel.args.syntax.Syntax.SyntaxResult;

public class ReflectParser< MODEL> extends AbstractParser< Result< MODEL>> {

	public ReflectParser( final Class< MODEL> resultType) {
		this( new GnuSyntax(), resultType);
	}

	private final Class< MODEL> model;
	private final Map< Method, MethodHandler> methodHandlers = new HashMap<>();

	public ReflectParser( final Syntax syntax, final Class< MODEL> model) {
		super( syntax);

		if( !model.isInterface())
			throw new IllegalArgumentException( model.getName() + " is not an interface");
		this.model = model;

		if( model.isAnnotationPresent( OperandPattern.class)) {
			final OperandPattern annotation = model.getAnnotation( OperandPattern.class);
			this.setOperandPattern( annotation.value());
			for( final OperandDescription description: annotation.descriptions())
				this.setOperandDescription( description.name(), description.description());
		}
		if( model.isAnnotationPresent( Resource.class))
			this.setResourceBundleBase( model.getAnnotation( Resource.class).value());
		if( model.isAnnotationPresent( Description.class))
			this.setDescription( model.getAnnotation( Description.class).value());

		for( final Method method: model.getMethods())
			if( !SimpleAccessor.class.equals( method.getDeclaringClass()))
				this.methodHandlers.put( method,
						method.isAnnotationPresent( Operands.class)? new OperandHandler( method, this.getOperandNames()): new OptionHandler(
								method, this));
	}

	@ Override
	protected Option addOption( final String primaryName, final String[] additionalNames, final boolean required,
			final boolean repeatable, final ArgumentPolicy argumentPolicy, final String description, final String argumentName) {
		return super.addOption( primaryName, additionalNames, required, repeatable, argumentPolicy, description, argumentName);
	}

	@ Override
	protected Result< MODEL> buildResult( final SyntaxResult syntaxResult, final Map< String, List< String>> operands,
			final Collection< ? extends ArgumentException> parseTimeExceptions) {
		final Collection< ArgumentException> exceptions = new LinkedHashSet<>( parseTimeExceptions);
		final SimpleAccessorImpl simpleAccessor = new SimpleAccessorImpl( syntaxResult, operands);

		final Map< Method, Object> decoded = new HashMap<>();
		for( final Entry< Method, MethodHandler> methodHandlerPair: this.methodHandlers.entrySet())
			if( !SimpleAccessor.class.equals( methodHandlerPair.getKey().getDeclaringClass()))
				decoded.put( methodHandlerPair.getKey(),
						methodHandlerPair.getValue().decode( simpleAccessor, new ExceptionHandler< DecoderException>() {

							@ Override
							public void handle( final DecoderException exception) {
								exceptions.add( exception);
							}
						}));

		return new AbstractResult< MODEL>( Collections.unmodifiableCollection( exceptions), this.model.cast( Proxy.newProxyInstance(
				this.model.getClassLoader(), new Class< ?>[]{ this.model}, new InvocationHandler() {

					@ Override
					public Object invoke( final Object proxy, final Method method, final Object[] args) throws IllegalAccessException,
							InvocationTargetException {
						if( SimpleAccessor.class.equals( method.getDeclaringClass()))
							return method.invoke( simpleAccessor, args);
						return decoded.get( method);
					}
				})));
	}

	public static < MODEL>Result< MODEL> parse( final Syntax syntax, final Class< MODEL> resultType, final String... args) {
		return new ReflectParser< MODEL>( syntax, resultType).parse( args);
	}

	public static < MODEL>Result< MODEL> parse( final Class< MODEL> resultType, final String... args) {
		return new ReflectParser< MODEL>( resultType).parse( args);
	}
}
