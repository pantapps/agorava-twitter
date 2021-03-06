/*
 * Copyright 2013 Agorava
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

package org.agorava.twitter.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.agorava.twitter.model.PlaceType;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
class PlaceMixin {

    @JsonCreator
    public PlaceMixin(@JsonProperty("id") String id, @JsonProperty("name") String name,
                      @JsonProperty("full_name") String fullName,
                      @JsonProperty("attributes") @JsonDeserialize(using = StreetAddressDeserializer.class) String
                              streetAddress,
                      @JsonProperty("country") String country, @JsonProperty("country_code") String countryCode,
                      @JsonProperty("place_type") @JsonDeserialize(using = PlaceTypeDeserializer.class) PlaceType placeType) {
    }

    private static class StreetAddressDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode tree = jp.readValueAsTree();
            return tree.has("street_address") ? tree.get("street_address").asText() : null;
        }
    }
}
