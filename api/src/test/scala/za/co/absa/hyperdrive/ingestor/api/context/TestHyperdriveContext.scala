/*
 * Copyright 2018 ABSA Group Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package za.co.absa.hyperdrive.ingestor.api.context

import org.scalatest.{FlatSpec, Matchers}

class TestHyperdriveContext extends FlatSpec with Matchers {
  behavior of HyperdriveContext.getClass.getName

  "get" should "return the value for the given key" in {
    HyperdriveContext.put("some-key", 1L)

    val value = HyperdriveContext.get[Long]("some-key")

    value.get shouldBe 1L
  }

  it should "return a failure with NoSuchElementException if key doesn't exist" in {
    HyperdriveContext.put("some-key", 1L)

    val value = HyperdriveContext.get[Long]("some-other-key")

    value.failed.get.getClass shouldBe classOf[NoSuchElementException]
    value.failed.get.getMessage should include("some-other-key")
  }

  it should "return a failure with ClassCastException if value cannot be cast to type" in {
    HyperdriveContext.put("some-key", Seq(1, 2, 3))

    val value = HyperdriveContext.get[Long]("some-key")

    value.failed.get.getClass shouldBe classOf[ClassCastException]
    value.failed.get.getMessage should include("some-key")
  }
}
