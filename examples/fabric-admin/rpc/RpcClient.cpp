/*
 *
 *    Copyright (c) 2024 Project CHIP Authors
 *    All rights reserved.
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

#include "RpcClient.h"
#include "RpcClientProcessor.h"

#include <chrono>
#include <condition_variable>
#include <mutex>
#include <string>
#include <thread>

#include "fabric_bridge_service/fabric_bridge_service.rpc.pb.h"
#include "pw_assert/check.h"
#include "pw_hdlc/decoder.h"
#include "pw_hdlc/default_addresses.h"
#include "pw_hdlc/rpc_channel.h"
#include "pw_rpc/client.h"
#include "pw_stream/socket_stream.h"

using namespace chip;

namespace {

// Constants
constexpr uint32_t kRpcTimeoutMs     = 1000;
constexpr uint32_t kDefaultChannelId = 1;

// Fabric Bridge Client
rpc::pw_rpc::nanopb::FabricBridge::Client fabricBridgeClient(rpc::client::GetDefaultRpcClient(), kDefaultChannelId);

std::mutex responseMutex;
std::condition_variable responseCv;
bool responseReceived    = false;
CHIP_ERROR responseError = CHIP_NO_ERROR;

// By passing the `call` parameter into WaitForResponse we are explicitly trying to insure the caller takes into consideration that
// the lifetime of the `call` object when calling WaitForResponse
template <typename CallType>
CHIP_ERROR WaitForResponse(CallType & call)
{
    std::unique_lock<std::mutex> lock(responseMutex);
    responseReceived = false;
    responseError    = CHIP_NO_ERROR;

    if (responseCv.wait_for(lock, std::chrono::milliseconds(kRpcTimeoutMs), [] { return responseReceived; }))
    {
        return responseError;
    }
    else
    {
        fprintf(stderr, "RPC Response timed out!");
        return CHIP_ERROR_TIMEOUT;
    }
}

// Callback function to be called when the RPC response is received
void OnAddDeviceResponseCompleted(const pw_protobuf_Empty & response, pw::Status status)
{
    std::lock_guard<std::mutex> lock(responseMutex);
    responseReceived = true;
    responseError    = status.ok() ? CHIP_NO_ERROR : CHIP_ERROR_INTERNAL;
    responseCv.notify_one();

    if (status.ok())
    {
        ChipLogProgress(NotSpecified, "AddSynchronizedDevice RPC call succeeded!");
    }
    else
    {
        ChipLogProgress(NotSpecified, "AddSynchronizedDevice RPC call failed with status: %d\n", status.code());
    }
}

// Callback function to be called when the RPC response is received
void OnRemoveDeviceResponseCompleted(const pw_protobuf_Empty & response, pw::Status status)
{
    std::lock_guard<std::mutex> lock(responseMutex);
    responseReceived = true;
    responseError    = status.ok() ? CHIP_NO_ERROR : CHIP_ERROR_INTERNAL;
    responseCv.notify_one();

    if (status.ok())
    {
        ChipLogProgress(NotSpecified, "RemoveSynchronizedDevice RPC call succeeded!");
    }
    else
    {
        ChipLogProgress(NotSpecified, "RemoveSynchronizedDevice RPC call failed with status: %d", status.code());
    }
}

} // namespace

CHIP_ERROR InitRpcClient(uint16_t rpcServerPort)
{
    rpc::client::SetRpcServerPort(rpcServerPort);
    return rpc::client::StartPacketProcessing();
}

CHIP_ERROR AddSynchronizedDevice(chip::NodeId nodeId)
{
    ChipLogProgress(NotSpecified, "AddSynchronizedDevice");

    chip_rpc_SynchronizedDevice device = chip_rpc_SynchronizedDevice_init_default;
    device.node_id                     = nodeId;

    // TODO: fill this with real data. For now we just add things for testing
    strcpy(device.vendor_name, "Test Vendor");
    device.has_vendor_name = true;

    device.vendor_id     = 123;
    device.has_vendor_id = true;

    strcpy(device.product_name, "Test Product");
    device.has_product_name = true;

    device.product_id     = 234;
    device.has_product_id = true;

    strcpy(device.node_label, "Device Label");
    device.has_node_label = true;

    device.hardware_version     = 11;
    device.has_hardware_version = true;

    strcpy(device.hardware_version_string, "Hardware");
    device.has_hardware_version_string = true;

    device.software_version     = 22;
    device.has_software_version = true;

    strcpy(device.software_version_string, "Test 1.4.22");
    device.has_software_version_string = true;

    // The RPC call is kept alive until it completes. When a response is received, it will be logged by the handler
    // function and the call will complete.
    auto call = fabricBridgeClient.AddSynchronizedDevice(device, OnAddDeviceResponseCompleted);

    if (!call.active())
    {
        // The RPC call was not sent. This could occur due to, for example, an invalid channel ID. Handle if necessary.
        return CHIP_ERROR_INTERNAL;
    }

    return WaitForResponse(call);
}

CHIP_ERROR RemoveSynchronizedDevice(chip::NodeId nodeId)
{
    ChipLogProgress(NotSpecified, "RemoveSynchronizedDevice");

    chip_rpc_SynchronizedDevice device = chip_rpc_SynchronizedDevice_init_default;
    device.node_id                     = nodeId;

    // The RPC call is kept alive until it completes. When a response is received, it will be logged by the handler
    // function and the call will complete.
    auto call = fabricBridgeClient.RemoveSynchronizedDevice(device, OnRemoveDeviceResponseCompleted);

    if (!call.active())
    {
        // The RPC call was not sent. This could occur due to, for example, an invalid channel ID. Handle if necessary.
        return CHIP_ERROR_INTERNAL;
    }

    return WaitForResponse(call);
}
