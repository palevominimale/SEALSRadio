package app.seals.radio.data.room

import app.seals.radio.data.models.StationModelDataCurrent
import app.seals.radio.data.models.StationModelDataFavorites
import app.seals.radio.entities.responses.StationModel

fun StationModelDataCurrent.mapToDomain() : StationModel {
    return StationModel(
        changeuuid,
        stationuuid,
        serveruuid,
        name,
        url,
        urlResolved,
        homepage,
        favicon,
        tags,
        country,
        countrycode,
        iso31662,
        state,
        language,
        languagecodes,
        votes,
        lastchangetime,
        lastchangetimeIso8601,
        codec,
        bitrate,
        hls,
        lastcheckok,
        lastchecktime,
        lastchecktimeIso8601,
        lastcheckoktime,
        lastcheckoktimeIso8601,
        lastlocalchecktime,
        lastlocalchecktimeIso8601,
        clicktimestamp,
        clicktimestampIso8601,
        clickcount,
        clicktrend,
        sslError,
        geoLat,
        geoLong,
        hasExtendedInfo
    )
}

fun StationModelDataFavorites.mapToDomain() : StationModel {
    return StationModel(
        changeuuid,
        stationuuid,
        serveruuid,
        name,
        url,
        urlResolved,
        homepage,
        favicon,
        tags,
        country,
        countrycode,
        iso31662,
        state,
        language,
        languagecodes,
        votes,
        lastchangetime,
        lastchangetimeIso8601,
        codec,
        bitrate,
        hls,
        lastcheckok,
        lastchecktime,
        lastchecktimeIso8601,
        lastcheckoktime,
        lastcheckoktimeIso8601,
        lastlocalchecktime,
        lastlocalchecktimeIso8601,
        clicktimestamp,
        clicktimestampIso8601,
        clickcount,
        clicktrend,
        sslError,
        geoLat,
        geoLong,
        hasExtendedInfo
    )
}

fun StationModel.mapToCurrent() : StationModelDataCurrent {
    return StationModelDataCurrent(
        changeuuid,
        stationuuid ?: "",
        serveruuid,
        name,
        url,
        urlResolved,
        homepage,
        favicon,
        tags,
        country,
        countrycode,
        iso31662,
        state,
        language,
        languagecodes,
        votes,
        lastchangetime,
        lastchangetimeIso8601,
        codec,
        bitrate,
        hls,
        lastcheckok,
        lastchecktime,
        lastchecktimeIso8601,
        lastcheckoktime,
        lastcheckoktimeIso8601,
        lastlocalchecktime,
        lastlocalchecktimeIso8601,
        clicktimestamp,
        clicktimestampIso8601,
        clickcount,
        clicktrend,
        sslError,
        geoLat,
        geoLong,
        hasExtendedInfo
    )
}

fun StationModel.mapToFavorite() : StationModelDataFavorites {
    return StationModelDataFavorites(
        changeuuid,
        stationuuid ?: "",
        serveruuid,
        name,
        url,
        urlResolved,
        homepage,
        favicon,
        tags,
        country,
        countrycode,
        iso31662,
        state,
        language,
        languagecodes,
        votes,
        lastchangetime,
        lastchangetimeIso8601,
        codec,
        bitrate,
        hls,
        lastcheckok,
        lastchecktime,
        lastchecktimeIso8601,
        lastcheckoktime,
        lastcheckoktimeIso8601,
        lastlocalchecktime,
        lastlocalchecktimeIso8601,
        clicktimestamp,
        clicktimestampIso8601,
        clickcount,
        clicktrend,
        sslError,
        geoLat,
        geoLong,
        hasExtendedInfo
    )
}

fun List<StationModelDataCurrent>.mapToDomain() : List<StationModel> {
    return mutableListOf<StationModel>().apply {
        this@mapToDomain.forEach {
            this.add(it.mapToDomain())
        }
    }
}

@JvmName("mapToDomainStationModelDataFavorites")
fun List<StationModelDataFavorites>.mapToDomain() : List<StationModel> {
    return mutableListOf<StationModel>().apply {
        this@mapToDomain.forEach {
            this.add(it.mapToDomain())
        }
    }
}

fun List<StationModel>.mapToCurrent() : List<StationModelDataCurrent> {
    return mutableListOf<StationModelDataCurrent>().apply {
        this@mapToCurrent.forEach {
            this.add(it.mapToCurrent())
        }
    }
}