/**
 *
 * Copyright (C) 2009 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package org.jclouds.aws.ec2.services;

import static org.jclouds.aws.ec2.reference.EC2Parameters.ACTION;
import static org.jclouds.aws.ec2.reference.EC2Parameters.VERSION;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import javax.annotation.Nullable;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.jclouds.aws.ec2.binders.BindInstanceIdsToIndexedFormParams;
import org.jclouds.aws.ec2.binders.IfNotNullBindAvailabilityZoneToFormParam;
import org.jclouds.aws.ec2.domain.AvailabilityZone;
import org.jclouds.aws.ec2.domain.InstanceStateChange;
import org.jclouds.aws.ec2.domain.InstanceType;
import org.jclouds.aws.ec2.domain.Region;
import org.jclouds.aws.ec2.domain.Reservation;
import org.jclouds.aws.ec2.domain.Image.EbsBlockDevice;
import org.jclouds.aws.ec2.domain.Volume.InstanceInitiatedShutdownBehavior;
import org.jclouds.aws.ec2.filters.FormSigner;
import org.jclouds.aws.ec2.functions.ConvertUnencodedBytesToBase64EncodedString;
import org.jclouds.aws.ec2.functions.RegionToEndpoint;
import org.jclouds.aws.ec2.options.RunInstancesOptions;
import org.jclouds.aws.ec2.xml.BlockDeviceMappingHandler;
import org.jclouds.aws.ec2.xml.BooleanValueHandler;
import org.jclouds.aws.ec2.xml.DescribeInstancesResponseHandler;
import org.jclouds.aws.ec2.xml.InstanceInitiatedShutdownBehaviorHandler;
import org.jclouds.aws.ec2.xml.InstanceStateChangeHandler;
import org.jclouds.aws.ec2.xml.InstanceTypeHandler;
import org.jclouds.aws.ec2.xml.RunInstancesResponseHandler;
import org.jclouds.aws.ec2.xml.StringValueHandler;
import org.jclouds.aws.ec2.xml.UnencodeStringValueHandler;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.EndpointParam;
import org.jclouds.rest.annotations.FormParams;
import org.jclouds.rest.annotations.ParamParser;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.VirtualHost;
import org.jclouds.rest.annotations.XMLResponseParser;

/**
 * Provides access to EC2 Instance Services via their REST API.
 * <p/>
 * 
 * @author Adrian Cole
 */
@RequestFilters(FormSigner.class)
@FormParams(keys = VERSION, values = "2009-11-30")
@VirtualHost
public interface InstanceAsyncClient {

   /**
    * @see InstanceClient#describeInstancesInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = ACTION, values = "DescribeInstances")
   @XMLResponseParser(DescribeInstancesResponseHandler.class)
   Future<? extends Set<Reservation>> describeInstancesInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @BinderParam(BindInstanceIdsToIndexedFormParams.class) String... instanceIds);

   /**
    * @see InstanceClient#runInstancesInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = ACTION, values = "RunInstances")
   @XMLResponseParser(RunInstancesResponseHandler.class)
   Future<Reservation> runInstancesInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @Nullable @BinderParam(IfNotNullBindAvailabilityZoneToFormParam.class) AvailabilityZone nullableAvailabilityZone,
            @FormParam("ImageId") String imageId, @FormParam("MinCount") int minCount,
            @FormParam("MaxCount") int maxCount, RunInstancesOptions... options);

   /**
    * @see InstanceClient#rebootInstancesInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = ACTION, values = "RebootInstances")
   Future<Void> rebootInstancesInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @BinderParam(BindInstanceIdsToIndexedFormParams.class) String... instanceIds);

   /**
    * @see InstanceClient#terminateInstancesInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = ACTION, values = "TerminateInstances")
   @XMLResponseParser(InstanceStateChangeHandler.class)
   Future<? extends Set<InstanceStateChange>> terminateInstancesInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @BinderParam(BindInstanceIdsToIndexedFormParams.class) String... instanceIds);

   /**
    * @see InstanceClient#stopInstancesInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = ACTION, values = "StopInstances")
   @XMLResponseParser(InstanceStateChangeHandler.class)
   Future<? extends Set<InstanceStateChange>> stopInstancesInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @FormParam("Force") boolean force,
            @BinderParam(BindInstanceIdsToIndexedFormParams.class) String... instanceIds);

   /**
    * @see InstanceClient#startInstancesInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = ACTION, values = "StartInstances")
   @XMLResponseParser(InstanceStateChangeHandler.class)
   Future<? extends Set<InstanceStateChange>> startInstancesInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @BinderParam(BindInstanceIdsToIndexedFormParams.class) String... instanceIds);

   /**
    * @see AMIClient#getUserDataForInstanceInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = { ACTION, "Attribute" }, values = { "DescribeInstanceAttribute", "userData" })
   @XMLResponseParser(UnencodeStringValueHandler.class)
   Future<String> getUserDataForInstanceInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @FormParam("InstanceId") String instanceId);

   /**
    * @see AMIClient#getRootDeviceNameForInstanceInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = { ACTION, "Attribute" }, values = { "DescribeInstanceAttribute",
            "rootDeviceName" })
   @XMLResponseParser(StringValueHandler.class)
   Future<String> getRootDeviceNameForInstanceInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @FormParam("InstanceId") String instanceId);

   /**
    * @see AMIClient#getRamdiskForInstanceInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = { ACTION, "Attribute" }, values = { "DescribeInstanceAttribute", "ramdisk" })
   @XMLResponseParser(StringValueHandler.class)
   Future<String> getRamdiskForInstanceInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @FormParam("InstanceId") String instanceId);

   /**
    * @see AMIClient#getKernelForInstanceInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = { ACTION, "Attribute" }, values = { "DescribeInstanceAttribute", "kernel" })
   @XMLResponseParser(StringValueHandler.class)
   Future<String> getKernelForInstanceInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @FormParam("InstanceId") String instanceId);

   /**
    * @see AMIClient#isApiTerminationDisabledForInstanceInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = { ACTION, "Attribute" }, values = { "DescribeInstanceAttribute",
            "disableApiTermination" })
   @XMLResponseParser(BooleanValueHandler.class)
   Future<Boolean> isApiTerminationDisabledForInstanceInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @FormParam("InstanceId") String instanceId);

   /**
    * @see AMIClient#getInstanceTypeForInstanceInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = { ACTION, "Attribute" }, values = { "DescribeInstanceAttribute",
            "instanceType" })
   @XMLResponseParser(InstanceTypeHandler.class)
   Future<InstanceType> getInstanceTypeForInstanceInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @FormParam("InstanceId") String instanceId);

   /**
    * @see AMIClient#getInstanceInitiatedShutdownBehaviorForInstanceInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = { ACTION, "Attribute" }, values = { "DescribeInstanceAttribute",
            "instanceInitiatedShutdownBehavior" })
   @XMLResponseParser(InstanceInitiatedShutdownBehaviorHandler.class)
   Future<InstanceInitiatedShutdownBehavior> getInstanceInitiatedShutdownBehaviorForInstanceInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @FormParam("InstanceId") String instanceId);

   /**
    * @see AMIClient#getBlockDeviceMappingForInstanceInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = { ACTION, "Attribute" }, values = { "DescribeInstanceAttribute",
            "blockDeviceMapping" })
   @XMLResponseParser(BlockDeviceMappingHandler.class)
   Future<? extends Map<String, EbsBlockDevice>> getBlockDeviceMappingForInstanceInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @FormParam("InstanceId") String instanceId);

   /**
    * @see AMIClient#resetRamdiskForInstanceInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = { ACTION, "Attribute" }, values = { "ResetInstanceAttribute", "ramdisk" })
   Future<Void> resetRamdiskForInstanceInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @FormParam("InstanceId") String instanceId);

   /**
    * @see AMIClient#resetKernelForInstanceInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = { ACTION, "Attribute" }, values = { "ResetInstanceAttribute", "kernel" })
   Future<Void> resetKernelForInstanceInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @FormParam("InstanceId") String instanceId);

   /**
    * @see AMIClient#setUserDataForInstanceInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = { ACTION, "Attribute" }, values = { "ModifyInstanceAttribute", "userData" })
   Future<Void> setUserDataForInstanceInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @FormParam("InstanceId") String instanceId,
            @FormParam("Value") @ParamParser(ConvertUnencodedBytesToBase64EncodedString.class) byte[] unencodedData);

   /**
    * @see AMIClient#setRamdiskForInstanceInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = { ACTION, "Attribute" }, values = { "ModifyInstanceAttribute", "ramdisk" })
   Future<Void> setRamdiskForInstanceInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @FormParam("InstanceId") String instanceId, @FormParam("Value") String ramdisk);

   /**
    * @see AMIClient#setKernelForInstanceInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = { ACTION, "Attribute" }, values = { "ModifyInstanceAttribute", "kernel" })
   Future<Void> setKernelForInstanceInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @FormParam("InstanceId") String instanceId, @FormParam("Value") String kernel);

   /**
    * @see AMIClient#setApiTerminationDisabledForInstanceInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = { ACTION, "Attribute" }, values = { "ModifyInstanceAttribute",
            "disableApiTermination" })
   Future<Void> setApiTerminationDisabledForInstanceInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @FormParam("InstanceId") String instanceId,
            @FormParam("Value") boolean apiTerminationDisabled);

   /**
    * @see AMIClient#setInstanceTypeForInstanceInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = { ACTION, "Attribute" }, values = { "ModifyInstanceAttribute", "instanceType" })
   Future<Void> setInstanceTypeForInstanceInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @FormParam("InstanceId") String instanceId,
            @FormParam("Value") InstanceType instanceType);

   /**
    * @see AMIClient#setInstanceInitiatedShutdownBehaviorForInstanceInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = { ACTION, "Attribute" }, values = { "ModifyInstanceAttribute",
            "instanceInitiatedShutdownBehavior" })
   Future<Void> setInstanceInitiatedShutdownBehaviorForInstanceInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @FormParam("InstanceId") String instanceId,
            @FormParam("Value") InstanceInitiatedShutdownBehavior instanceInitiatedShutdownBehavior);

   /**
    * @see AMIClient#setBlockDeviceMappingForInstanceInRegion
    */
   @POST
   @Path("/")
   @FormParams(keys = { ACTION, "Attribute" }, values = { "ModifyInstanceAttribute",
            "blockDeviceMapping" })
   Future<Void> setBlockDeviceMappingForInstanceInRegion(
            @EndpointParam(parser = RegionToEndpoint.class) Region region,
            @FormParam("InstanceId") String instanceId,
            @FormParam("Value") String blockDeviceMapping);

}