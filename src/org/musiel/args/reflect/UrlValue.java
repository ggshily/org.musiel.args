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

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.MalformedURLException;
import java.net.URL;

@ Retention( RetentionPolicy.RUNTIME)
@ Target( ElementType.METHOD)
@ Inherited
@ DecoderAnnotation( UrlValue.Decoder.class)
public @ interface UrlValue {

	static class Decoder implements org.musiel.args.reflect.Decoder< URL> {

		@ Override
		public URL decode( final String string) throws DecoderException {
			try {
				return new URL( string);
			} catch( final MalformedURLException exception) {
				throw new DecoderException( UrlValue.class.getPackage().getName() + ".exceptions", "constraint", "mulformed URL", string);
			}
		}
	}
}