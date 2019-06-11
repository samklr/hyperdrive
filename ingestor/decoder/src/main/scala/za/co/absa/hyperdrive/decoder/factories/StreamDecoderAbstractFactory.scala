/*
 *  Copyright 2019 ABSA Group Limited
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package za.co.absa.hyperdrive.decoder.factories

import org.apache.commons.configuration2.Configuration
import org.apache.logging.log4j.LogManager
import za.co.absa.hyperdrive.decoder.{StreamDecoder, StreamDecoderFactory}
import za.co.absa.hyperdrive.decoder.factories.confluent.avro.ConfluentAvroKafkaStreamDecoderFactory

import scala.util.{Failure, Success, Try}

/**
  * Abstract factory for stream decoders.
  *
  * After creating a new StreamDecoder implementation, add the corresponding factory to "factoryMap" inside this class.
  */
object StreamDecoderAbstractFactory {

  private val logger = LogManager.getLogger
  val componentConfigKey = "component.decoder"

  private val factoryMap = Map[String,StreamDecoderFactory](
    ConfluentAvroKafkaStreamDecoderFactory.name.toLowerCase -> ConfluentAvroKafkaStreamDecoderFactory)

  def build(config: Configuration): StreamDecoder = {

    logger.info(s"Going to load factory for configuration '$componentConfigKey'.")

    val factoryName = config.getString(componentConfigKey)

    Try(factoryMap(factoryName.toLowerCase)) match {
      case Success(factory) => factory.build(config)
      case Failure(exception) => throw new IllegalArgumentException(s"Invalid StreamDecoderFactory name: '$factoryName'.", exception)
    }
  }

  def getAvailableFactories: Set[String] = factoryMap.keys.toSet
}