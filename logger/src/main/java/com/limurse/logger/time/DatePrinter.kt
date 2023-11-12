package com.limurse.logger.time

import java.text.FieldPosition
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 *
 * DatePrinter is the "missing" interface for the format methods of
 * [java.text.DateFormat].
 *
 * @since 3.2
 */
interface DatePrinter {
    /**
     *
     * Formats a millisecond `long` value.
     *
     * @param millis the millisecond value to format
     * @return the formatted string
     * @since 2.1
     */
    fun format(millis: Long): String?

    /**
     *
     * Formats a `Date` object using a `GregorianCalendar`.
     *
     * @param date the date to format
     * @return the formatted string
     */
    fun format(date: Date?): String?

    /**
     *
     * Formats a `Calendar` object.
     *
     * @param calendar the calendar to format
     * @return the formatted string
     */
    fun format(calendar: Calendar?): String?

    /**
     *
     * Formats a milliseond `long` value into the
     * supplied `StringBuffer`.
     *
     * @param millis the millisecond value to format
     * @param buf    the buffer to format into
     * @return the specified string buffer
     */
    fun format(millis: Long, buf: StringBuffer?): StringBuffer?

    /**
     *
     * Formats a `Date` object into the
     * supplied `StringBuffer` using a `GregorianCalendar`.
     *
     * @param date the date to format
     * @param buf  the buffer to format into
     * @return the specified string buffer
     */
    fun format(date: Date?, buf: StringBuffer?): StringBuffer?

    /**
     *
     * Formats a `Calendar` object into the
     * supplied `StringBuffer`.
     *
     * @param calendar the calendar to format
     * @param buf      the buffer to format into
     * @return the specified string buffer
     */
    fun format(calendar: Calendar?, buf: StringBuffer?): StringBuffer?
    // Accessors
    //-----------------------------------------------------------------------
    /**
     *
     * Gets the pattern used by this printer.
     *
     * @return the pattern, [java.text.SimpleDateFormat] compatible
     */
    val pattern: String?

    /**
     *
     * Gets the time zone used by this printer.
     *
     *
     *
     * This zone is always used for `Date` printing.
     *
     * @return the time zone
     */
    val timeZone: TimeZone?

    /**
     *
     * Gets the locale used by this printer.
     *
     * @return the locale
     */
    val locale: Locale?

    /**
     *
     * Formats a `Date`, `Calendar` or
     * `Long` (milliseconds) object.
     *
     *
     * See [java.text.DateFormat.format]
     *
     * @param obj        the object to format
     * @param toAppendTo the buffer to append to
     * @param pos        the position - ignored
     * @return the buffer passed in
     */
    fun format(obj: Any?, toAppendTo: StringBuffer?, pos: FieldPosition?): StringBuffer?
}