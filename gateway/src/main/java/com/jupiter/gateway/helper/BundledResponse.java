package com.jupiter.gateway.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.Map;

@Getter
public class BundledResponse {
    private Map<String, Object> responses;
    private final long timestamp = System.currentTimeMillis();
    
    public BundledResponse(List<PartialResponse> partialResponses) {
        responses = StreamEx.of(partialResponses)
                            .map(PartialResponse::trim)
                            .toMap(PartialResponse::getEndpointUri, PartialResponse::getApiResponse);
    }
    
    @AllArgsConstructor(staticName="from") @Getter
    public static class PartialResponse {
        private String endpointUri;
        private Map<String, Object> apiResponse;
        
        private static final String[] REDUNDANT_KEYS = { "timestamp" };
        
        /**
         * Remove redundant information from individual ApiResponse to optimize payload size
         * 
         */
        public PartialResponse trim() {
            StreamEx.of(REDUNDANT_KEYS)
                    .forEach(key -> apiResponse.remove(key));
            
            return this;
        }
    }
}
