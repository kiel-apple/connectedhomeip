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
package chip.devicecontroller.cluster.structs

import chip.devicecontroller.cluster.*
import java.util.Optional
import matter.tlv.ContextSpecificTag
import matter.tlv.Tag
import matter.tlv.TlvReader
import matter.tlv.TlvWriter

class CameraAvStreamManagementClusterSnapshotCapabilitiesStruct(
  val resolution: CameraAvStreamManagementClusterVideoResolutionStruct,
  val maxFrameRate: UInt,
  val imageCodec: UInt,
  val requiresEncodedPixels: Boolean,
  val requiresHardwareEncoder: Optional<Boolean>,
) {
  override fun toString(): String = buildString {
    append("CameraAvStreamManagementClusterSnapshotCapabilitiesStruct {\n")
    append("\tresolution : $resolution\n")
    append("\tmaxFrameRate : $maxFrameRate\n")
    append("\timageCodec : $imageCodec\n")
    append("\trequiresEncodedPixels : $requiresEncodedPixels\n")
    append("\trequiresHardwareEncoder : $requiresHardwareEncoder\n")
    append("}\n")
  }

  fun toTlv(tlvTag: Tag, tlvWriter: TlvWriter) {
    tlvWriter.apply {
      startStructure(tlvTag)
      resolution.toTlv(ContextSpecificTag(TAG_RESOLUTION), this)
      put(ContextSpecificTag(TAG_MAX_FRAME_RATE), maxFrameRate)
      put(ContextSpecificTag(TAG_IMAGE_CODEC), imageCodec)
      put(ContextSpecificTag(TAG_REQUIRES_ENCODED_PIXELS), requiresEncodedPixels)
      if (requiresHardwareEncoder.isPresent) {
        val optrequiresHardwareEncoder = requiresHardwareEncoder.get()
        put(ContextSpecificTag(TAG_REQUIRES_HARDWARE_ENCODER), optrequiresHardwareEncoder)
      }
      endStructure()
    }
  }

  companion object {
    private const val TAG_RESOLUTION = 0
    private const val TAG_MAX_FRAME_RATE = 1
    private const val TAG_IMAGE_CODEC = 2
    private const val TAG_REQUIRES_ENCODED_PIXELS = 3
    private const val TAG_REQUIRES_HARDWARE_ENCODER = 4

    fun fromTlv(
      tlvTag: Tag,
      tlvReader: TlvReader,
    ): CameraAvStreamManagementClusterSnapshotCapabilitiesStruct {
      tlvReader.enterStructure(tlvTag)
      val resolution =
        CameraAvStreamManagementClusterVideoResolutionStruct.fromTlv(
          ContextSpecificTag(TAG_RESOLUTION),
          tlvReader,
        )
      val maxFrameRate = tlvReader.getUInt(ContextSpecificTag(TAG_MAX_FRAME_RATE))
      val imageCodec = tlvReader.getUInt(ContextSpecificTag(TAG_IMAGE_CODEC))
      val requiresEncodedPixels =
        tlvReader.getBoolean(ContextSpecificTag(TAG_REQUIRES_ENCODED_PIXELS))
      val requiresHardwareEncoder =
        if (tlvReader.isNextTag(ContextSpecificTag(TAG_REQUIRES_HARDWARE_ENCODER))) {
          Optional.of(tlvReader.getBoolean(ContextSpecificTag(TAG_REQUIRES_HARDWARE_ENCODER)))
        } else {
          Optional.empty()
        }

      tlvReader.exitContainer()

      return CameraAvStreamManagementClusterSnapshotCapabilitiesStruct(
        resolution,
        maxFrameRate,
        imageCodec,
        requiresEncodedPixels,
        requiresHardwareEncoder,
      )
    }
  }
}
