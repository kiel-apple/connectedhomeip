/*
 *
 *    Copyright (c) 2023 Project CHIP Authors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package matter.devicecontroller.cluster.clusters

import matter.devicecontroller.cluster.structs.*

class LaundryWasherControlsCluster(private val endpointId: UShort) {
  class SpinSpeedsAttribute(val value: List<String>?)

  class SpinSpeedCurrentAttribute(val value: UByte?)

  class SupportedRinsesAttribute(val value: List<UInt>?)

  class GeneratedCommandListAttribute(val value: List<UInt>)

  class AcceptedCommandListAttribute(val value: List<UInt>)

  class EventListAttribute(val value: List<UInt>)

  class AttributeListAttribute(val value: List<UInt>)

  suspend fun readSpinSpeedsAttribute(): SpinSpeedsAttribute {
    // Implementation needs to be added here
  }

  suspend fun subscribeSpinSpeedsAttribute(
    minInterval: Int,
    maxInterval: Int
  ): SpinSpeedsAttribute {
    // Implementation needs to be added here
  }

  suspend fun readSpinSpeedCurrentAttribute(): SpinSpeedCurrentAttribute {
    // Implementation needs to be added here
  }

  suspend fun writeSpinSpeedCurrentAttribute(value: UByte) {
    // Implementation needs to be added here
  }

  suspend fun writeSpinSpeedCurrentAttribute(value: UByte, timedWriteTimeoutMs: Int) {
    // Implementation needs to be added here
  }

  suspend fun subscribeSpinSpeedCurrentAttribute(
    minInterval: Int,
    maxInterval: Int
  ): SpinSpeedCurrentAttribute {
    // Implementation needs to be added here
  }

  suspend fun readNumberOfRinsesAttribute(): UByte {
    // Implementation needs to be added here
  }

  suspend fun writeNumberOfRinsesAttribute(value: UInt) {
    // Implementation needs to be added here
  }

  suspend fun writeNumberOfRinsesAttribute(value: UInt, timedWriteTimeoutMs: Int) {
    // Implementation needs to be added here
  }

  suspend fun subscribeNumberOfRinsesAttribute(minInterval: Int, maxInterval: Int): UByte {
    // Implementation needs to be added here
  }

  suspend fun readSupportedRinsesAttribute(): SupportedRinsesAttribute {
    // Implementation needs to be added here
  }

  suspend fun subscribeSupportedRinsesAttribute(
    minInterval: Int,
    maxInterval: Int
  ): SupportedRinsesAttribute {
    // Implementation needs to be added here
  }

  suspend fun readGeneratedCommandListAttribute(): GeneratedCommandListAttribute {
    // Implementation needs to be added here
  }

  suspend fun subscribeGeneratedCommandListAttribute(
    minInterval: Int,
    maxInterval: Int
  ): GeneratedCommandListAttribute {
    // Implementation needs to be added here
  }

  suspend fun readAcceptedCommandListAttribute(): AcceptedCommandListAttribute {
    // Implementation needs to be added here
  }

  suspend fun subscribeAcceptedCommandListAttribute(
    minInterval: Int,
    maxInterval: Int
  ): AcceptedCommandListAttribute {
    // Implementation needs to be added here
  }

  suspend fun readEventListAttribute(): EventListAttribute {
    // Implementation needs to be added here
  }

  suspend fun subscribeEventListAttribute(minInterval: Int, maxInterval: Int): EventListAttribute {
    // Implementation needs to be added here
  }

  suspend fun readAttributeListAttribute(): AttributeListAttribute {
    // Implementation needs to be added here
  }

  suspend fun subscribeAttributeListAttribute(
    minInterval: Int,
    maxInterval: Int
  ): AttributeListAttribute {
    // Implementation needs to be added here
  }

  suspend fun readFeatureMapAttribute(): UInt {
    // Implementation needs to be added here
  }

  suspend fun subscribeFeatureMapAttribute(minInterval: Int, maxInterval: Int): UInt {
    // Implementation needs to be added here
  }

  suspend fun readClusterRevisionAttribute(): UShort {
    // Implementation needs to be added here
  }

  suspend fun subscribeClusterRevisionAttribute(minInterval: Int, maxInterval: Int): UShort {
    // Implementation needs to be added here
  }

  companion object {
    const val CLUSTER_ID: UInt = 83u
  }
}