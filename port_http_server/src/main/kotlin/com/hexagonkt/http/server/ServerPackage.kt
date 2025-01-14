package com.hexagonkt.http.server

import com.hexagonkt.helpers.CodedException

/** Alias for routes' and filters' callbacks. Functions executed when a route is matched. */
typealias RouteCallback = Call.() -> Unit

/** Alias for exceptions' callbacks. Functions executed when an exception is thrown. */
typealias ExceptionCallback = Call.(Exception) -> Unit

/** Alias for errors' callbacks. Functions executed to handle a HTTP error code. */
typealias ErrorCodeCallback = Call.(CodedException) -> Unit
