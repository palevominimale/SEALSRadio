package app.seals.radio.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Current")
data class StationModelDataCurrent(
    @ColumnInfo(name="changeuuid"                 )     val changeuuid                : String?  = null,
    @PrimaryKey val stationuuid                                                       : String  = "",
    @ColumnInfo(name="serveruuid"                 )     val serveruuid                : String?  = null,
    @ColumnInfo(name="name"                       )     val name                      : String?  = null,
    @ColumnInfo(name="url"                        )     val url                       : String?  = null,
    @ColumnInfo(name="url_resolved"               )     val urlResolved               : String?  = null,
    @ColumnInfo(name="homepage"                   )     val homepage                  : String?  = null,
    @ColumnInfo(name="favicon"                    )     val favicon                   : String?  = null,
    @ColumnInfo(name="tags"                       )     val tags                      : String?  = null,
    @ColumnInfo(name="country"                    )     val country                   : String?  = null,
    @ColumnInfo(name="countrycode"                )     val countrycode               : String?  = null,
    @ColumnInfo(name="iso_3166_2"                 )     val iso31662                  : String?  = null,
    @ColumnInfo(name="state"                      )     val state                     : String?  = null,
    @ColumnInfo(name="language"                   )     val language                  : String?  = null,
    @ColumnInfo(name="languagecodes"              )     val languagecodes             : String?  = null,
    @ColumnInfo(name="votes"                      )     val votes                     : Int?     = null,
    @ColumnInfo(name="lastchangetime"             )     val lastchangetime            : String?  = null,
    @ColumnInfo(name="lastchangetime_iso8601"     )     val lastchangetimeIso8601     : String?  = null,
    @ColumnInfo(name="codec"                      )     val codec                     : String?  = null,
    @ColumnInfo(name="bitrate"                    )     val bitrate                   : Int?     = null,
    @ColumnInfo(name="hls"                        )     val hls                       : Int?     = null,
    @ColumnInfo(name="lastcheckok"                )     val lastcheckok               : Int?     = null,
    @ColumnInfo(name="lastchecktime"              )     val lastchecktime             : String?  = null,
    @ColumnInfo(name="lastchecktime_iso8601"      )     val lastchecktimeIso8601      : String?  = null,
    @ColumnInfo(name="lastcheckoktime"            )     val lastcheckoktime           : String?  = null,
    @ColumnInfo(name="lastcheckoktime_iso8601"    )     val lastcheckoktimeIso8601    : String?  = null,
    @ColumnInfo(name="lastlocalchecktime"         )     val lastlocalchecktime        : String?  = null,
    @ColumnInfo(name="lastlocalchecktime_iso8601" )     val lastlocalchecktimeIso8601 : String?  = null,
    @ColumnInfo(name="clicktimestamp"             )     val clicktimestamp            : String?  = null,
    @ColumnInfo(name="clicktimestamp_iso8601"     )     val clicktimestampIso8601     : String?  = null,
    @ColumnInfo(name="clickcount"                 )     val clickcount                : Int?     = null,
    @ColumnInfo(name="clicktrend"                 )     val clicktrend                : Int?     = null,
    @ColumnInfo(name="ssl_error"                  )     val sslError                  : Int?     = null,
    @ColumnInfo(name="geo_lat"                    )     val geoLat                    : String?  = null,
    @ColumnInfo(name="geo_long"                   )     val geoLong                   : String?  = null,
    @ColumnInfo(name="has_extended_info"          )     val hasExtendedInfo           : Boolean? = null
) : Serializable