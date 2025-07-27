package ro.alingrosu.stockmanagement.presentation.util

class Event<T>(content: T?) {
    private val mContent: T?

    private var hasBeenHandled = false


    init {
        requireNotNull(content) { "null values in Event are not allowed." }
        mContent = content
    }

    val contentIfNotHandled: T?
        get() {
            if (hasBeenHandled) {
                return null
            } else {
                hasBeenHandled = true
                return mContent
            }
        }

    fun hasBeenHandled(): Boolean {
        return hasBeenHandled
    }
}