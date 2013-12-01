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

package org.agorava.twitter.model;

import org.agorava.api.function.Identifiable;
import org.agorava.api.function.Nameable;

/**
 * Represents a place that a Twitter user may tweet from.
 *
 * @author Craig Walls
 * @author Antoine Sabot-Durand
 * @author Werner Keil
 */
public class Place implements Identifiable, Nameable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -9112165211756900468L;

	private final String id;

    private final String name;

    private final String fullName;

    private final String streetAddress;

    private final String country;

    private final String countryCode;

    private final PlaceType placeType;

    public Place(String id, String name, String fullName, String streetAddress, String country, String countryCode,
                 PlaceType placeType) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.country = country;
        this.streetAddress = streetAddress;
        this.countryCode = countryCode;
        this.placeType = placeType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public PlaceType getPlaceType() {
        return placeType;
    }

}
