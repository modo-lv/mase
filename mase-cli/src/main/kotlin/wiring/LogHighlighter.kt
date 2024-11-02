package wiring

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.pattern.color.ANSIConstants.*
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase

/**
 * Customize logging output colors for different levels.
 */
class LogHighlighter : ForegroundCompositeConverterBase<ILoggingEvent>() {
    override fun getForegroundColorCode(event: ILoggingEvent): String {
        return when (event.level) {
            Level.ERROR -> BOLD + RED_FG
            Level.WARN -> BOLD + YELLOW_FG
            Level.INFO -> BOLD + WHITE_FG
            Level.DEBUG -> WHITE_FG
            Level.TRACE -> BOLD + BLACK_FG
            else -> DEFAULT_FG
        }
    }
}