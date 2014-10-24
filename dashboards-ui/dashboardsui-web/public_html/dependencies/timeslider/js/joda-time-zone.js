define(['jquery'], function($) {
    var timeZoneList = [{
            "offset": "-12:00",
            "location": "Etc/GMT+12"
        },
        {
            "offset": "-11:00",
            "location": "Etc/GMT+11"
        },
        {
            "offset": "-11:00",
            "location": "Pacific/Apia"
        },
        {
            "offset": "-11:00",
            "location": "Pacific/Midway"
        },
        {
            "offset": "-11:00",
            "location": "Pacific/Niue"
        },
        {
            "offset": "-11:00",
            "location": "Pacific/Pago_Pago"
        },
        {
            "offset": "-10:00",
            "location": "America/Adak"
        },
        {
            "offset": "-10:00",
            "location": "Etc/GMT+10"
        },
        {
            "offset": "-10:00",
            "location": "HST"
        },
        {
            "offset": "-10:00",
            "location": "Pacific/Fakaofo"
        },
        {
            "offset": "-10:00",
            "location": "Pacific/Honolulu"
        },
        {
            "offset": "-10:00",
            "location": "Pacific/Johnston"
        },
        {
            "offset": "-10:00",
            "location": "Pacific/Rarotonga"
        },
        {
            "offset": "-10:00",
            "location": "Pacific/Tahiti"
        },
        {
            "offset": "-09:30",
            "location": "Pacific/Marquesas"
        },
        {
            "offset": "-09:00",
            "location": "America/Anchorage"
        },
        {
            "offset": "-09:00",
            "location": "America/Juneau"
        },
        {
            "offset": "-09:00",
            "location": "America/Nome"
        },
        {
            "offset": "-09:00",
            "location": "America/Yakutat"
        },
        {
            "offset": "-09:00",
            "location": "Etc/GMT+9"
        },
        {
            "offset": "-09:00",
            "location": "Pacific/Gambier"
        },
        {
            "offset": "-08:00",
            "location": "America/Dawson"
        },
        {
            "offset": "-08:00",
            "location": "America/Los_Angeles"
        },
        {
            "offset": "-08:00",
            "location": "America/Santa_Isabel"
        },
        {
            "offset": "-08:00",
            "location": "America/Tijuana"
        },
        {
            "offset": "-08:00",
            "location": "America/Vancouver"
        },
        {
            "offset": "-08:00",
            "location": "America/Whitehorse"
        },
        {
            "offset": "-08:00",
            "location": "Etc/GMT+8"
        },
        {
            "offset": "-08:00",
            "location": "PST8PDT"
        },
        {
            "offset": "-08:00",
            "location": "Pacific/Pitcairn"
        },
        {
            "offset": "-07:00",
            "location": "America/Boise"
        },
        {
            "offset": "-07:00",
            "location": "America/Cambridge_Bay"
        },
        {
            "offset": "-07:00",
            "location": "America/Chihuahua"
        },
        {
            "offset": "-07:00",
            "location": "America/Dawson_Creek"
        },
        {
            "offset": "-07:00",
            "location": "America/Denver"
        },
        {
            "offset": "-07:00",
            "location": "America/Edmonton"
        },
        {
            "offset": "-07:00",
            "location": "America/Hermosillo"
        },
        {
            "offset": "-07:00",
            "location": "America/Inuvik"
        },
        {
            "offset": "-07:00",
            "location": "America/Mazatlan"
        },
        {
            "offset": "-07:00",
            "location": "America/Ojinaga"
        },
        {
            "offset": "-07:00",
            "location": "America/Phoenix"
        },
        {
            "offset": "-07:00",
            "location": "America/Yellowknife"
        },
        {
            "offset": "-07:00",
            "location": "Etc/GMT+7"
        },
        {
            "offset": "-07:00",
            "location": "MST"
        },
        {
            "offset": "-07:00",
            "location": "MST7MDT"
        },
        {
            "offset": "-06:00",
            "location": "America/Bahia_Banderas"
        },
        {
            "offset": "-06:00",
            "location": "America/Belize"
        },
        {
            "offset": "-06:00",
            "location": "America/Cancun"
        },
        {
            "offset": "-06:00",
            "location": "America/Chicago"
        },
        {
            "offset": "-06:00",
            "location": "America/Costa_Rica"
        },
        {
            "offset": "-06:00",
            "location": "America/El_Salvador"
        },
        {
            "offset": "-06:00",
            "location": "America/Guatemala"
        },
        {
            "offset": "-06:00",
            "location": "America/Indiana/Knox"
        },
        {
            "offset": "-06:00",
            "location": "America/Indiana/Tell_City"
        },
        {
            "offset": "-06:00",
            "location": "America/Managua"
        },
        {
            "offset": "-06:00",
            "location": "America/Matamoros"
        },
        {
            "offset": "-06:00",
            "location": "America/Menominee"
        },
        {
            "offset": "-06:00",
            "location": "America/Merida"
        },
        {
            "offset": "-06:00",
            "location": "America/Mexico_City"
        },
        {
            "offset": "-06:00",
            "location": "America/Monterrey"
        },
        {
            "offset": "-06:00",
            "location": "America/North_Dakota/Center"
        },
        {
            "offset": "-06:00",
            "location": "America/North_Dakota/New_Salem"
        },
        {
            "offset": "-06:00",
            "location": "America/Rainy_River"
        },
        {
            "offset": "-06:00",
            "location": "America/Rankin_Inlet"
        },
        {
            "offset": "-06:00",
            "location": "America/Regina"
        },
        {
            "offset": "-06:00",
            "location": "America/Swift_Current"
        },
        {
            "offset": "-06:00",
            "location": "America/Tegucigalpa"
        },
        {
            "offset": "-06:00",
            "location": "America/Winnipeg"
        },
        {
            "offset": "-06:00",
            "location": "CST6CDT"
        },
        {
            "offset": "-06:00",
            "location": "Etc/GMT+6"
        },
        {
            "offset": "-06:00",
            "location": "Pacific/Easter"
        },
        {
            "offset": "-06:00",
            "location": "Pacific/Galapagos"
        },
        {
            "offset": "-05:00",
            "location": "America/Atikokan"
        },
        {
            "offset": "-05:00",
            "location": "America/Bogota"
        },
        {
            "offset": "-05:00",
            "location": "America/Cayman"
        },
        {
            "offset": "-05:00",
            "location": "America/Detroit"
        },
        {
            "offset": "-05:00",
            "location": "America/Grand_Turk"
        },
        {
            "offset": "-05:00",
            "location": "America/Guayaquil"
        },
        {
            "offset": "-05:00",
            "location": "America/Havana"
        },
        {
            "offset": "-05:00",
            "location": "America/Indiana/Indianapolis"
        },
        {
            "offset": "-05:00",
            "location": "America/Indiana/Marengo"
        },
        {
            "offset": "-05:00",
            "location": "America/Indiana/Petersburg"
        },
        {
            "offset": "-05:00",
            "location": "America/Indiana/Vevay"
        },
        {
            "offset": "-05:00",
            "location": "America/Indiana/Vincennes"
        },
        {
            "offset": "-05:00",
            "location": "America/Indiana/Winamac"
        },
        {
            "offset": "-05:00",
            "location": "America/Iqaluit"
        },
        {
            "offset": "-05:00",
            "location": "America/Jamaica"
        },
        {
            "offset": "-05:00",
            "location": "America/Kentucky/Louisville"
        },
        {
            "offset": "-05:00",
            "location": "America/Kentucky/Monticello"
        },
        {
            "offset": "-05:00",
            "location": "America/Lima"
        },
        {
            "offset": "-05:00",
            "location": "America/Montreal"
        },
        {
            "offset": "-05:00",
            "location": "America/Nassau"
        },
        {
            "offset": "-05:00",
            "location": "America/New_York"
        },
        {
            "offset": "-05:00",
            "location": "America/Nipigon"
        },
        {
            "offset": "-05:00",
            "location": "America/Panama"
        },
        {
            "offset": "-05:00",
            "location": "America/Pangnirtung"
        },
        {
            "offset": "-05:00",
            "location": "America/Port-au-Prince"
        },
        {
            "offset": "-05:00",
            "location": "America/Resolute"
        },
        {
            "offset": "-05:00",
            "location": "America/Thunder_Bay"
        },
        {
            "offset": "-05:00",
            "location": "America/Toronto"
        },
        {
            "offset": "-05:00",
            "location": "EST"
        },
        {
            "offset": "-05:00",
            "location": "EST5EDT"
        },
        {
            "offset": "-05:00",
            "location": "Etc/GMT+5"
        },
        {
            "offset": "-04:30",
            "location": "America/Caracas"
        },
        {
            "offset": "-04:00",
            "location": "America/Anguilla"
        },
        {
            "offset": "-04:00",
            "location": "America/Antigua"
        },
        {
            "offset": "-03:00",
            "location": "America/Argentina/San_Luis"
        },
        {
            "offset": "-04:00",
            "location": "America/Aruba"
        },
        {
            "offset": "-04:00",
            "location": "America/Asuncion"
        },
        {
            "offset": "-04:00",
            "location": "America/Barbados"
        },
        {
            "offset": "-04:00",
            "location": "America/Blanc-Sablon"
        },
        {
            "offset": "-04:00",
            "location": "America/Boa_Vista"
        },
        {
            "offset": "-04:00",
            "location": "America/Campo_Grande"
        },
        {
            "offset": "-04:00",
            "location": "America/Cuiaba"
        },
        {
            "offset": "-04:00",
            "location": "America/Curacao"
        },
        {
            "offset": "-04:00",
            "location": "America/Dominica"
        },
        {
            "offset": "-04:00",
            "location": "America/Eirunepe"
        },
        {
            "offset": "-04:00",
            "location": "America/Glace_Bay"
        },
        {
            "offset": "-04:00",
            "location": "America/Goose_Bay"
        },
        {
            "offset": "-04:00",
            "location": "America/Grenada"
        },
        {
            "offset": "-04:00",
            "location": "America/Guadeloupe"
        },
        {
            "offset": "-04:00",
            "location": "America/Guyana"
        },
        {
            "offset": "-04:00",
            "location": "America/Halifax"
        },
        {
            "offset": "-04:00",
            "location": "America/La_Paz"
        },
        {
            "offset": "-04:00",
            "location": "America/Manaus"
        },
        {
            "offset": "-04:00",
            "location": "America/Martinique"
        },
        {
            "offset": "-04:00",
            "location": "America/Moncton"
        },
        {
            "offset": "-04:00",
            "location": "America/Montserrat"
        },
        {
            "offset": "-04:00",
            "location": "America/Port_of_Spain"
        },
        {
            "offset": "-04:00",
            "location": "America/Porto_Velho"
        },
        {
            "offset": "-04:00",
            "location": "America/Puerto_Rico"
        },
        {
            "offset": "-04:00",
            "location": "America/Rio_Branco"
        },
        {
            "offset": "-04:00",
            "location": "America/Santiago"
        },
        {
            "offset": "-04:00",
            "location": "America/Santo_Domingo"
        },
        {
            "offset": "-04:00",
            "location": "America/St_Kitts"
        },
        {
            "offset": "-04:00",
            "location": "America/St_Lucia"
        },
        {
            "offset": "-04:00",
            "location": "America/St_Thomas"
        },
        {
            "offset": "-04:00",
            "location": "America/St_Vincent"
        },
        {
            "offset": "-04:00",
            "location": "America/Thule"
        },
        {
            "offset": "-04:00",
            "location": "America/Tortola"
        },
        {
            "offset": "-04:00",
            "location": "Antarctica/Palmer"
        },
        {
            "offset": "-04:00",
            "location": "Atlantic/Bermuda"
        },
        {
            "offset": "-04:00",
            "location": "Atlantic/Stanley"
        },
        {
            "offset": "-04:00",
            "location": "Etc/GMT+4"
        },
        {
            "offset": "-03:30",
            "location": "America/St_Johns"
        },
        {
            "offset": "-03:00",
            "location": "America/Araguaina"
        },
        {
            "offset": "-03:00",
            "location": "America/Argentina/Buenos_Aires"
        },
        {
            "offset": "-03:00",
            "location": "America/Argentina/Catamarca"
        },
        {
            "offset": "-03:00",
            "location": "America/Argentina/Cordoba"
        },
        {
            "offset": "-03:00",
            "location": "America/Argentina/Jujuy"
        },
        {
            "offset": "-03:00",
            "location": "America/Argentina/La_Rioja"
        },
        {
            "offset": "-03:00",
            "location": "America/Argentina/Mendoza"
        },
        {
            "offset": "-03:00",
            "location": "America/Argentina/Rio_Gallegos"
        },
        {
            "offset": "-03:00",
            "location": "America/Argentina/Salta"
        },
        {
            "offset": "-03:00",
            "location": "America/Argentina/San_Juan"
        },
        {
            "offset": "-03:00",
            "location": "America/Argentina/Tucuman"
        },
        {
            "offset": "-03:00",
            "location": "America/Argentina/Ushuaia"
        },
        {
            "offset": "-03:00",
            "location": "America/Bahia"
        },
        {
            "offset": "-03:00",
            "location": "America/Belem"
        },
        {
            "offset": "-03:00",
            "location": "America/Cayenne"
        },
        {
            "offset": "-03:00",
            "location": "America/Fortaleza"
        },
        {
            "offset": "-03:00",
            "location": "America/Godthab"
        },
        {
            "offset": "-03:00",
            "location": "America/Maceio"
        },
        {
            "offset": "-03:00",
            "location": "America/Miquelon"
        },
        {
            "offset": "-03:00",
            "location": "America/Montevideo"
        },
        {
            "offset": "-03:00",
            "location": "America/Paramaribo"
        },
        {
            "offset": "-03:00",
            "location": "America/Recife"
        },
        {
            "offset": "-03:00",
            "location": "America/Santarem"
        },
        {
            "offset": "-03:00",
            "location": "America/Sao_Paulo"
        },
        {
            "offset": "-03:00",
            "location": "Antarctica/Rothera"
        },
        {
            "offset": "-03:00",
            "location": "Etc/GMT+3"
        },
        {
            "offset": "-02:00",
            "location": "America/Noronha"
        },
        {
            "offset": "-02:00",
            "location": "Atlantic/South_Georgia"
        },
        {
            "offset": "-02:00",
            "location": "Etc/GMT+2"
        },
        {
            "offset": "-01:00",
            "location": "America/Scoresbysund"
        },
        {
            "offset": "-01:00",
            "location": "Atlantic/Azores"
        },
        {
            "offset": "-01:00",
            "location": "Atlantic/Cape_Verde"
        },
        {
            "offset": "-01:00",
            "location": "Etc/GMT+1"
        },
        {
            "offset": "+00:00",
            "location": "Africa/Abidjan"
        },
        {
            "offset": "+00:00",
            "location": "Africa/Accra"
        },
        {
            "offset": "+00:00",
            "location": "Africa/Bamako"
        },
        {
            "offset": "+00:00",
            "location": "Africa/Banjul"
        },
        {
            "offset": "+00:00",
            "location": "Africa/Bissau"
        },
        {
            "offset": "+00:00",
            "location": "Africa/Casablanca"
        },
        {
            "offset": "+00:00",
            "location": "Africa/Conakry"
        },
        {
            "offset": "+00:00",
            "location": "Africa/Dakar"
        },
        {
            "offset": "+00:00",
            "location": "Africa/El_Aaiun"
        },
        {
            "offset": "+00:00",
            "location": "Africa/Freetown"
        },
        {
            "offset": "+00:00",
            "location": "Africa/Lome"
        },
        {
            "offset": "+00:00",
            "location": "Africa/Monrovia"
        },
        {
            "offset": "+00:00",
            "location": "Africa/Nouakchott"
        },
        {
            "offset": "+00:00",
            "location": "Africa/Ouagadougou"
        },
        {
            "offset": "+00:00",
            "location": "Africa/Sao_Tome"
        },
        {
            "offset": "+00:00",
            "location": "America/Danmarkshavn"
        },
        {
            "offset": "+00:00",
            "location": "Atlantic/Canary"
        },
        {
            "offset": "+00:00",
            "location": "Atlantic/Faroe"
        },
        {
            "offset": "+00:00",
            "location": "Atlantic/Madeira"
        },
        {
            "offset": "+00:00",
            "location": "Atlantic/Reykjavik"
        },
        {
            "offset": "+00:00",
            "location": "Atlantic/St_Helena"
        },
        {
            "offset": "+00:00",
            "location": "Etc/GMT"
        },
        {
            "offset": "+00:00",
            "location": "Etc/UCT"
        },
        {
            "offset": "+00:00",
            "location": "Etc/UTC"
        },
        {
            "offset": "+00:00",
            "location": "Europe/Dublin"
        },
        {
            "offset": "+00:00",
            "location": "Europe/Lisbon"
        },
        {
            "offset": "+00:00",
            "location": "Europe/London"
        },
        {
            "offset": "+00:00",
            "location": "UTC"
        },
        {
            "offset": "+00:00",
            "location": "WET"
        },
        {
            "offset": "+01:00",
            "location": "Africa/Algiers"
        },
        {
            "offset": "+01:00",
            "location": "Africa/Bangui"
        },
        {
            "offset": "+01:00",
            "location": "Africa/Brazzaville"
        },
        {
            "offset": "+01:00",
            "location": "Africa/Ceuta"
        },
        {
            "offset": "+01:00",
            "location": "Africa/Douala"
        },
        {
            "offset": "+01:00",
            "location": "Africa/Kinshasa"
        },
        {
            "offset": "+01:00",
            "location": "Africa/Lagos"
        },
        {
            "offset": "+01:00",
            "location": "Africa/Libreville"
        },
        {
            "offset": "+01:00",
            "location": "Africa/Luanda"
        },
        {
            "offset": "+01:00",
            "location": "Africa/Malabo"
        },
        {
            "offset": "+01:00",
            "location": "Africa/Ndjamena"
        },
        {
            "offset": "+01:00",
            "location": "Africa/Niamey"
        },
        {
            "offset": "+01:00",
            "location": "Africa/Porto-Novo"
        },
        {
            "offset": "+01:00",
            "location": "Africa/Tunis"
        },
        {
            "offset": "+01:00",
            "location": "Africa/Windhoek"
        },
        {
            "offset": "+01:00",
            "location": "CET"
        },
        {
            "offset": "+01:00",
            "location": "Etc/GMT-1"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Amsterdam"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Andorra"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Belgrade"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Berlin"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Brussels"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Budapest"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Copenhagen"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Gibraltar"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Luxembourg"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Madrid"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Malta"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Monaco"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Oslo"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Paris"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Prague"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Rome"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Stockholm"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Tirane"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Vaduz"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Vienna"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Warsaw"
        },
        {
            "offset": "+01:00",
            "location": "Europe/Zurich"
        },
        {
            "offset": "+01:00",
            "location": "MET"
        },
        {
            "offset": "+02:00",
            "location": "Africa/Blantyre"
        },
        {
            "offset": "+02:00",
            "location": "Africa/Bujumbura"
        },
        {
            "offset": "+02:00",
            "location": "Africa/Cairo"
        },
        {
            "offset": "+02:00",
            "location": "Africa/Gaborone"
        },
        {
            "offset": "+02:00",
            "location": "Africa/Harare"
        },
        {
            "offset": "+02:00",
            "location": "Africa/Johannesburg"
        },
        {
            "offset": "+02:00",
            "location": "Africa/Kigali"
        },
        {
            "offset": "+02:00",
            "location": "Africa/Lubumbashi"
        },
        {
            "offset": "+02:00",
            "location": "Africa/Lusaka"
        },
        {
            "offset": "+02:00",
            "location": "Africa/Maputo"
        },
        {
            "offset": "+02:00",
            "location": "Africa/Maseru"
        },
        {
            "offset": "+02:00",
            "location": "Africa/Mbabane"
        },
        {
            "offset": "+02:00",
            "location": "Africa/Tripoli"
        },
        {
            "offset": "+02:00",
            "location": "Asia/Amman"
        },
        {
            "offset": "+02:00",
            "location": "Asia/Beirut"
        },
        {
            "offset": "+02:00",
            "location": "Asia/Damascus"
        },
        {
            "offset": "+02:00",
            "location": "Asia/Gaza"
        },
        {
            "offset": "+02:00",
            "location": "Asia/Jerusalem"
        },
        {
            "offset": "+02:00",
            "location": "Asia/Nicosia"
        },
        {
            "offset": "+02:00",
            "location": "EET"
        },
        {
            "offset": "+02:00",
            "location": "Etc/GMT-2"
        },
        {
            "offset": "+02:00",
            "location": "Europe/Athens"
        },
        {
            "offset": "+02:00",
            "location": "Europe/Bucharest"
        },
        {
            "offset": "+02:00",
            "location": "Europe/Chisinau"
        },
        {
            "offset": "+02:00",
            "location": "Europe/Helsinki"
        },
        {
            "offset": "+02:00",
            "location": "Europe/Istanbul"
        },
        {
            "offset": "+02:00",
            "location": "Europe/Kaliningrad"
        },
        {
            "offset": "+02:00",
            "location": "Europe/Kiev"
        },
        {
            "offset": "+02:00",
            "location": "Europe/Minsk"
        },
        {
            "offset": "+02:00",
            "location": "Europe/Riga"
        },
        {
            "offset": "+02:00",
            "location": "Europe/Simferopol"
        },
        {
            "offset": "+02:00",
            "location": "Europe/Sofia"
        },
        {
            "offset": "+02:00",
            "location": "Europe/Tallinn"
        },
        {
            "offset": "+02:00",
            "location": "Europe/Uzhgorod"
        },
        {
            "offset": "+02:00",
            "location": "Europe/Vilnius"
        },
        {
            "offset": "+02:00",
            "location": "Europe/Zaporozhye"
        },
        {
            "offset": "+03:00",
            "location": "Africa/Addis_Ababa"
        },
        {
            "offset": "+03:00",
            "location": "Africa/Asmara"
        },
        {
            "offset": "+03:00",
            "location": "Africa/Dar_es_Salaam"
        },
        {
            "offset": "+03:00",
            "location": "Africa/Djibouti"
        },
        {
            "offset": "+03:00",
            "location": "Africa/Kampala"
        },
        {
            "offset": "+03:00",
            "location": "Africa/Khartoum"
        },
        {
            "offset": "+03:00",
            "location": "Africa/Mogadishu"
        },
        {
            "offset": "+03:00",
            "location": "Africa/Nairobi"
        },
        {
            "offset": "+03:00",
            "location": "Antarctica/Syowa"
        },
        {
            "offset": "+03:00",
            "location": "Asia/Aden"
        },
        {
            "offset": "+03:00",
            "location": "Asia/Baghdad"
        },
        {
            "offset": "+03:00",
            "location": "Asia/Bahrain"
        },
        {
            "offset": "+03:00",
            "location": "Asia/Kuwait"
        },
        {
            "offset": "+03:00",
            "location": "Asia/Qatar"
        },
        {
            "offset": "+03:00",
            "location": "Asia/Riyadh"
        },
        {
            "offset": "+03:00",
            "location": "Etc/GMT-3"
        },
        {
            "offset": "+03:00",
            "location": "Europe/Moscow"
        },
        {
            "offset": "+03:00",
            "location": "Europe/Samara"
        },
        {
            "offset": "+03:00",
            "location": "Europe/Volgograd"
        },
        {
            "offset": "+03:00",
            "location": "Indian/Antananarivo"
        },
        {
            "offset": "+03:00",
            "location": "Indian/Comoro"
        },
        {
            "offset": "+03:00",
            "location": "Indian/Mayotte"
        },
        {
            "offset": "+03:30",
            "location": "Asia/Tehran"
        },
        {
            "offset": "+04:00",
            "location": "Asia/Baku"
        },
        {
            "offset": "+04:00",
            "location": "Asia/Dubai"
        },
        {
            "offset": "+04:00",
            "location": "Asia/Muscat"
        },
        {
            "offset": "+04:00",
            "location": "Asia/Tbilisi"
        },
        {
            "offset": "+04:00",
            "location": "Asia/Yerevan"
        },
        {
            "offset": "+04:00",
            "location": "Etc/GMT-4"
        },
        {
            "offset": "+04:00",
            "location": "Indian/Mahe"
        },
        {
            "offset": "+04:00",
            "location": "Indian/Mauritius"
        },
        {
            "offset": "+04:00",
            "location": "Indian/Reunion"
        },
        {
            "offset": "+04:30",
            "location": "Asia/Kabul"
        },
        {
            "offset": "+05:00",
            "location": "Antarctica/Mawson"
        },
        {
            "offset": "+05:00",
            "location": "Asia/Aqtau"
        },
        {
            "offset": "+05:00",
            "location": "Asia/Aqtobe"
        },
        {
            "offset": "+05:00",
            "location": "Asia/Ashgabat"
        },
        {
            "offset": "+05:00",
            "location": "Asia/Dushanbe"
        },
        {
            "offset": "+05:00",
            "location": "Asia/Karachi"
        },
        {
            "offset": "+05:00",
            "location": "Asia/Oral"
        },
        {
            "offset": "+05:00",
            "location": "Asia/Samarkand"
        },
        {
            "offset": "+05:00",
            "location": "Asia/Tashkent"
        },
        {
            "offset": "+05:00",
            "location": "Asia/Yekaterinburg"
        },
        {
            "offset": "+05:00",
            "location": "Etc/GMT-5"
        },
        {
            "offset": "+05:00",
            "location": "Indian/Kerguelen"
        },
        {
            "offset": "+05:00",
            "location": "Indian/Maldives"
        },
        {
            "offset": "+05:30",
            "location": "Asia/Colombo"
        },
        {
            "offset": "+05:30",
            "location": "Asia/Kolkata"
        },
        {
            "offset": "+05:45",
            "location": "Asia/Kathmandu"
        },
        {
            "offset": "+06:00",
            "location": "Antarctica/Vostok"
        },
        {
            "offset": "+06:00",
            "location": "Asia/Almaty"
        },
        {
            "offset": "+06:00",
            "location": "Asia/Bishkek"
        },
        {
            "offset": "+06:00",
            "location": "Asia/Dhaka"
        },
        {
            "offset": "+06:00",
            "location": "Asia/Novokuznetsk"
        },
        {
            "offset": "+06:00",
            "location": "Asia/Novosibirsk"
        },
        {
            "offset": "+06:00",
            "location": "Asia/Omsk"
        },
        {
            "offset": "+06:00",
            "location": "Asia/Qyzylorda"
        },
        {
            "offset": "+06:00",
            "location": "Asia/Thimphu"
        },
        {
            "offset": "+06:00",
            "location": "Etc/GMT-6"
        },
        {
            "offset": "+06:00",
            "location": "Indian/Chagos"
        },
        {
            "offset": "+06:30",
            "location": "Asia/Rangoon"
        },
        {
            "offset": "+06:30",
            "location": "Indian/Cocos"
        },
        {
            "offset": "+07:00",
            "location": "Antarctica/Davis"
        },
        {
            "offset": "+07:00",
            "location": "Asia/Bangkok"
        },
        {
            "offset": "+07:00",
            "location": "Asia/Ho_Chi_Minh"
        },
        {
            "offset": "+07:00",
            "location": "Asia/Hovd"
        },
        {
            "offset": "+07:00",
            "location": "Asia/Jakarta"
        },
        {
            "offset": "+07:00",
            "location": "Asia/Krasnoyarsk"
        },
        {
            "offset": "+07:00",
            "location": "Asia/Phnom_Penh"
        },
        {
            "offset": "+07:00",
            "location": "Asia/Pontianak"
        },
        {
            "offset": "+07:00",
            "location": "Asia/Vientiane"
        },
        {
            "offset": "+07:00",
            "location": "Etc/GMT-7"
        },
        {
            "offset": "+07:00",
            "location": "Indian/Christmas"
        },
        {
            "offset": "+08:00",
            "location": "Antarctica/Casey"
        },
        {
            "offset": "+08:00",
            "location": "Asia/Brunei"
        },
        {
            "offset": "+08:00",
            "location": "Asia/Choibalsan"
        },
        {
            "offset": "+08:00",
            "location": "Asia/Chongqing"
        },
        {
            "offset": "+08:00",
            "location": "Asia/Harbin"
        },
        {
            "offset": "+08:00",
            "location": "Asia/Hong_Kong"
        },
        {
            "offset": "+08:00",
            "location": "Asia/Irkutsk"
        },
        {
            "offset": "+08:00",
            "location": "Asia/Kashgar"
        },
        {
            "offset": "+08:00",
            "location": "Asia/Kuala_Lumpur"
        },
        {
            "offset": "+08:00",
            "location": "Asia/Kuching"
        },
        {
            "offset": "+08:00",
            "location": "Asia/Macau"
        },
        {
            "offset": "+08:00",
            "location": "Asia/Makassar"
        },
        {
            "offset": "+08:00",
            "location": "Asia/Manila"
        },
        {
            "offset": "+08:00",
            "location": "Asia/Shanghai"
        },
        {
            "offset": "+08:00",
            "location": "Asia/Singapore"
        },
        {
            "offset": "+08:00",
            "location": "Asia/Taipei"
        },
        {
            "offset": "+08:00",
            "location": "Asia/Ulaanbaatar"
        },
        {
            "offset": "+08:00",
            "location": "Asia/Urumqi"
        },
        {
            "offset": "+08:00",
            "location": "Australia/Perth"
        },
        {
            "offset": "+08:00",
            "location": "Etc/GMT-8"
        },
        {
            "offset": "+08:45",
            "location": "Australia/Eucla"
        },
        {
            "offset": "+09:00",
            "location": "Asia/Dili"
        },
        {
            "offset": "+09:00",
            "location": "Asia/Jayapura"
        },
        {
            "offset": "+09:00",
            "location": "Asia/Pyongyang"
        },
        {
            "offset": "+09:00",
            "location": "Asia/Seoul",
            "location": "ROK"
        },
        {
            "offset": "+09:00",
            "location": "Asia/Tokyo"
        },
        {
            "offset": "+09:00",
            "location": "Asia/Yakutsk"
        },
        {
            "offset": "+09:00",
            "location": "Etc/GMT-9"
        },
        {
            "offset": "+09:00",
            "location": "Pacific/Palau"
        },
        {
            "offset": "+09:30",
            "location": "Australia/Adelaide"
        },
        {
            "offset": "+09:30",
            "location": "Australia/Broken_Hill"
        },
        {
            "offset": "+09:30",
            "location": "Australia/Darwin"
        },
        {
            "offset": "+10:00",
            "location": "Antarctica/DumontDUrville"
        },
        {
            "offset": "+10:00",
            "location": "Asia/Sakhalin"
        },
        {
            "offset": "+10:00",
            "location": "Asia/Vladivostok"
        },
        {
            "offset": "+10:00",
            "location": "Australia/Brisbane"
        },
        {
            "offset": "+10:00",
            "location": "Australia/Currie"
        },
        {
            "offset": "+10:00",
            "location": "Australia/Hobart"
        },
        {
            "offset": "+10:00",
            "location": "Australia/Lindeman"
        },
        {
            "offset": "+10:00",
            "location": "Australia/Melbourne"
        },
        {
            "offset": "+10:00",
            "location": "Australia/Sydney"
        },
        {
            "offset": "+10:00",
            "location": "Etc/GMT-10"
        },
        {
            "offset": "+10:00",
            "location": "Pacific/Chuuk"
        },
        {
            "offset": "+10:00",
            "location": "Pacific/Guam"
        },
        {
            "offset": "+10:00",
            "location": "Pacific/Port_Moresby"
        },
        {
            "offset": "+10:00",
            "location": "Pacific/Saipan"
        },
        {
            "offset": "+10:30",
            "location": "Australia/Lord_Howe"
        },
        {
            "offset": "+11:00",
            "location": "Antarctica/Macquarie"
        },
        {
            "offset": "+11:00",
            "location": "Asia/Anadyr"
        },
        {
            "offset": "+11:00",
            "location": "Asia/Kamchatka"
        },
        {
            "offset": "+11:00",
            "location": "Asia/Magadan"
        },
        {
            "offset": "+11:00",
            "location": "Etc/GMT-11"
        },
        {
            "offset": "+11:00",
            "location": "Pacific/Efate"
        },
        {
            "offset": "+11:00",
            "location": "Pacific/Guadalcanal"
        },
        {
            "offset": "+11:00",
            "location": "Pacific/Kosrae"
        },
        {
            "offset": "+11:00",
            "location": "Pacific/Noumea"
        },
        {
            "offset": "+11:00",
            "location": "Pacific/Pohnpei"
        },
        {
            "offset": "+11:30",
            "location": "Pacific/Norfolk"
        },
        {
            "offset": "+12:00",
            "location": "Antarctica/McMurdo"
        },
        {
            "offset": "+12:00",
            "location": "Etc/GMT-12"
        },
        {
            "offset": "+12:00",
            "location": "Pacific/Auckland"
        },
        {
            "offset": "+12:00",
            "location": "Pacific/Fiji"
        },
        {
            "offset": "+12:00",
            "location": "Pacific/Funafuti"
        },
        {
            "offset": "+12:00",
            "location": "Pacific/Kwajalein"
        },
        {
            "offset": "+12:00",
            "location": "Pacific/Majuro"
        },
        {
            "offset": "+12:00",
            "location": "Pacific/Nauru"
        },
        {
            "offset": "+12:00",
            "location": "Pacific/Tarawa"
        },
        {
            "offset": "+12:00",
            "location": "Pacific/Wake"
        },
        {
            "offset": "+12:00",
            "location": "Pacific/Wallis"
        },
        {
            "offset": "+12:45",
            "location": "Pacific/Chatham"
        },
        {
            "offset": "+13:00",
            "location": "Etc/GMT-13"
        },
        {
            "offset": "+13:00",
            "location": "Pacific/Enderbury"
        },
        {
            "offset": "+13:00",
            "location": "Pacific/Tongatapu"
        },
        {
            "offset": "+14:00",
            "location": "Etc/GMT-14"
        },
        {
            "offset": "+14:00",
            "location": "Pacific/Kiritimati"
        }
    ];
    
    $.each(timeZoneList, function() {
        this.displayName = "( " + this.offset + " ) " + this.location;
        
        var self = this;
        this.getTimeStamp = function(date) {
            var expr = self.offset.replace(/([\+\-])(\d+):(\d+)/, "-($1(Number('$2')*60+Number('$3'))*60*1000)");
            expr = date.getTime() + expr;
            return eval(expr);
        };
    });
    
    function fromLocation(location) {
        var timeZone;
        $.each(timeZoneList, function() {
            if (this.location === location) {
                timeZone = this;
                return false;
            }
        });
        return timeZone;
    }
    
    return  {
        timeZoneList: timeZoneList,
        fromLocation: fromLocation
    };
});
