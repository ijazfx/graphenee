/*******************************************************************************
 * Copyright (c) 2016, 2017, Graphenee
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.graphenee.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.graphenee.core.GrapheneeCoreConfiguration;

@Configuration
@AutoConfigureAfter(GrapheneeCoreConfiguration.class)
@ComponentScan("com.graphenee.aws")
@ConfigurationProperties
public class GrapheneeAwsConfiguration {

	@Value("${aws.secretKey:#{null}}")
	private String awsSecretKey;

	@Value("${aws.accessKeyId:#{null}}")
	private String awsAccessKeyId;

	@Bean
	public AWSCredentialsProvider awsCredentialProvider() {
		return new AWSCredentialsProvider() {

			@Override
			public void refresh() {
			}

			@Override
			public AWSCredentials getCredentials() {
				return new AWSCredentials() {

					@Override
					public String getAWSSecretKey() {
						return awsSecretKey;
					}

					@Override
					public String getAWSAccessKeyId() {
						return awsAccessKeyId;
					}
				};
			}
		};
	}

}
