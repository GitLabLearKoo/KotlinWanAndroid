package app.itgungnir.kwa.common.widget.list_footer

data class FooterStatus(val status: Status) {

    enum class Status {
        PROGRESSING,
        NO_MORE,
        SUCCEED,
        FAILED
    }
}