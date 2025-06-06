package com.jolupbisang.demo.infrastructure.aws.sfn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.audio.dto.StepFunctionInput;
import com.jolupbisang.demo.application.audio.dto.StepFunctionOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sfn.SfnClient;
import software.amazon.awssdk.services.sfn.model.SfnException;
import software.amazon.awssdk.services.sfn.model.StartSyncExecutionRequest;
import software.amazon.awssdk.services.sfn.model.StartSyncExecutionResponse;
import software.amazon.awssdk.services.sfn.model.SyncExecutionStatus;

@Slf4j
@Component
@RequiredArgsConstructor
public class SfnClientUtil {

    private final SfnClient sfnClient;
    private final ObjectMapper objectMapper;

    @Value("${cloud.aws.s3.bucket}")
    private String s3BucketName;

    public StepFunctionOutput startMergeAudioStateMachine(String stateMachineArn, Long meetingId) {
        StepFunctionInput stepFunctionInput = new StepFunctionInput(s3BucketName, meetingId);

        try {
            String inputJson = objectMapper.writeValueAsString(stepFunctionInput);

            StartSyncExecutionRequest executionRequest = StartSyncExecutionRequest.builder()
                    .stateMachineArn(stateMachineArn)
                    .input(inputJson)
                    .build();

            log.debug("Attempting to start sync execution for ARN: {} with input: {}", stateMachineArn, inputJson);
            StartSyncExecutionResponse response = sfnClient.startSyncExecution(executionRequest);
            log.debug("Sync execution response received. Status: {}, Execution ARN: {}", response.status(), response.executionArn());

            if (response.status() == SyncExecutionStatus.SUCCEEDED) {
                String outputJson = response.output();
                if (outputJson == null || outputJson.isEmpty()) {
                    log.warn("Step Functions execution succeeded for ARN: {} but output was null or empty.", response.executionArn());
                    return null;
                }
                log.info("Sync execution succeeded. Output JSON: {}", outputJson);
                return objectMapper.readValue(outputJson, StepFunctionOutput.class);
            } else {
                log.error("Step Functions execution failed for ARN: {}. Status: {}, Error: {}, Cause: {}",
                        response.executionArn(), response.status(), response.error(), response.cause());
            }
        } catch (JsonProcessingException e) {
            log.error("Error serializing Step Functions input for ARN: {} with meetingId: {} in SfnClientUtil", stateMachineArn, meetingId, e);
        } catch (SfnException e) {
            log.error("Error starting Step Functions execution for ARN: {} with meetingId: {} in SfnClientUtil: {}", stateMachineArn, meetingId, e.awsErrorDetails().errorMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error during Step Functions invocation for ARN: {} with meetingId: {} in SfnClientUtil", stateMachineArn, meetingId, e);
        }

        return null;
    }
}
