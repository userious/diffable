/**
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.diffable.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.Channel;

/**
 * IO utilities methods.
 * 
 * @author ibrahim Chaehoi
 */
public class IOUtils {
	
	/**
	 * Close the input stream
	 * 
	 * @param stream the input stream to close
	 */
	public static void close(InputStream stream) {

		if (stream != null) {

			try {
				stream.close();
			} catch (IOException e) {
				// Nothing to do
			}
		}
	}

	/**
	 * Close the output stream
	 * 
	 * @param stream the output stream to close
	 */
	public static void close(OutputStream stream) {

		if (stream != null) {

			try {
				stream.close();
			} catch (IOException e) {
				// Nothing to do
			}
		}
	}

	/**
	 * Close the channel
	 * 
	 * @param channel the channel to close
	 */
	public static void close(Channel channel) {
		if (channel != null) {

			try {
				channel.close();
			} catch (IOException e) {
				// Nothing to do
			}
		}
	}

	/**
	 * Close the reader
	 * @param reader the reader to close
	 */
	public static void close(Reader reader) {
		if (reader != null) {

			try {
				reader.close();
			} catch (IOException e) {
				// Nothing to do
			}
		}
	}
	
	/**
	 * Close the writer
	 * @param writer the writer to close
	 */
	public static void close(Writer writer) {
		if (writer != null) {

			try {
				writer.close();
			} catch (IOException e) {
				// Nothing to do
			}
		}
	}
}
