package com.sportz.base.business.domain.model

import java.io.Serializable

data class User(
    val city: String?,
    val countryId: String?,
    val countryName: String?,
    val dob: String?,
    val firstName: String?,
    val lastName: String?,
    val countryCode: String?,
    val mobileNo: String?,
    val email: String?,
    val status: Int?,
    val state: String?,
    val stateId: String?,
    val athleteId: String? = null,
    val teamId: String? = null,
    val marketingConsent : String?

) : Serializable {

    constructor(
        firstName: String?,
        lastName: String?,
        mobileNo: String?,
        countryCode: String?,
        countryId: String?,
        countryName: String?,
        stateId: String?,
        stateName: String?,
        city: String?,
        dob: String?,
        email: String?,
        athleteId: String? = null,
        teamId: String? = null,
        marketingConsent: String?
    ) : this(
        firstName = firstName,
        lastName = lastName,
        email = email,
        mobileNo = mobileNo,
        countryId = countryId,
        countryName = countryName,
        countryCode = countryCode,
        state = stateName,
        stateId = stateId,
        city = city,
        dob = dob,
        status = null,
        athleteId = athleteId,
        teamId = teamId,
        marketingConsent = marketingConsent
    )
}
