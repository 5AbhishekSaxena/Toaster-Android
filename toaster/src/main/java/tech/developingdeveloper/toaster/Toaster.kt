package tech.developingdeveloper.toaster

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import tech.developingdeveloper.toaster.utils.Colors


/**
 * @author Abhishek Saxena
 * @since 17-08-2020 06:25
 */

class Toaster private constructor(
    private val context: Context,
    private var message: CharSequence,
    private var duration: Int
) {

    private var rootView: View? = null
    private var leftDrawableRes: Int? = null

    companion object {
        const val LENGTH_SHORT = Toast.LENGTH_SHORT
        const val LENGTH_LONG = Toast.LENGTH_LONG

        fun pop(
            context: Context,
            message: CharSequence,
            duration: Int
        ): Toast {
            return pop(prepare(context, message, duration))
        }

        fun pop(
            context: Context,
            message: CharSequence,
            drawableRes: Int,
            duration: Int
        ): Toast {
            return pop(prepare(context, message, drawableRes, duration))
        }

        fun pop(toaster: Toaster): Toast {
            return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                Toast(toaster.context).apply {
                    duration = toaster.duration
                    view = toaster.rootView
                }
            } else {
                Toast.makeText(toaster.context, toaster.message, toaster.duration)
            }
        }

        fun popSuccess(
            context: Context,
            message: CharSequence,
            duration: Int
        ): Toast {
            return pop(prepareSuccess(context, message, duration))
        }

        fun popWarning(
            context: Context,
            message: CharSequence,
            duration: Int
        ): Toast {
            return pop(prepareWarning(context, message, duration))
        }

        fun popError(
            context: Context,
            message: CharSequence,
            duration: Int
        ): Toast {
            return pop(prepareError(context, message, duration))
        }

        private fun prepare(context: Context, message: CharSequence, duration: Int): Toaster {
            return prepare(context, message, null, duration)
        }

        private fun prepare(
            context: Context,
            message: CharSequence,
            drawableRes: Int?,
            duration: Int
        ): Toaster {
            return Builder(context)
                .setMessage(message)
                .apply {
                    drawableRes?.let {
                        setLeftDrawable(it)
                    }
                }
                .setDuration(duration)
                .make()
        }

        private fun prepareSuccess(
            context: Context,
            message: CharSequence,
            duration: Int
        ): Toaster {
            return Builder(context)
                .setMessage(message)
                .setLeftDrawable(R.drawable.ic_baseline_check_circle_24)
                .setLeftDrawableTint(Colors.SUCCESS)
                .setStripTint(Colors.SUCCESS)
                .setDuration(duration)
                .make()
        }

        private fun prepareWarning(
            context: Context,
            message: CharSequence,
            duration: Int
        ): Toaster {
            return Builder(context)
                .setMessage(message)
                .setLeftDrawable(R.drawable.ic_baseline_warning_24)
                .setLeftDrawableTint(Colors.WARNING)
                .setStripTint(Colors.WARNING)
                .setDuration(duration)
                .make()
        }

        private fun prepareError(context: Context, message: CharSequence, duration: Int): Toaster {
            return Builder(context)
                .setMessage(message)
                .setLeftDrawable(R.drawable.ic_baseline_error_24)
                .setLeftDrawableTint(Colors.ERROR)
                .setStripTint(Colors.ERROR)
                .setDuration(duration)
                .make()
        }
    }

    class Builder(private val context: Context) {

        private val rootView: View
        private var messageTextView: TextView? = null
        private var leftDrawableImageView: ImageView? = null
        private var leftDrawableRes: Int? = null
        private var colorStripView: View? = null

        private var message: CharSequence = ""
        private var duration: Int = LENGTH_SHORT

        init {
            rootView = initView(context)
        }

        private fun initView(context: Context): View {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rootView = inflater.inflate(R.layout.layout_toast, ConstraintLayout(context), false)

            messageTextView = rootView.findViewById(R.id.message_text_view)
            leftDrawableImageView = rootView.findViewById(R.id.left_drawable_image_view)
            colorStripView = rootView.findViewById(R.id.color_strip_view)

            setInitViewProperties()

            return rootView
        }

        fun setMessage(message: CharSequence) = apply {
            this.message = message
            messageTextView?.text = message
            messageTextView?.visibility = View.VISIBLE
        }

        fun setLeftDrawable(leftDrawableRes: Int) = apply {
            this.leftDrawableRes = leftDrawableRes
            leftDrawableImageView?.setImageResource(leftDrawableRes)
            leftDrawableImageView?.visibility = View.VISIBLE
        }

        fun setDuration(duration: Int) = apply {
            this.duration = duration
        }

        fun setLeftDrawableTint(colorRes: Int) = apply {
            leftDrawableImageView?.setColorFilter(ContextCompat.getColor(context, colorRes))
            leftDrawableImageView?.visibility = View.VISIBLE
        }

        fun setStripTint(colorRes: Int) = apply {
            colorStripView?.setBackgroundColor(ContextCompat.getColor(this.context, colorRes))
            colorStripView?.visibility = View.VISIBLE
        }

        fun make(): Toaster {
            return Toaster(context, message, duration).also {
                it.rootView = rootView
                it.leftDrawableRes = leftDrawableRes
            }
        }

        private fun setInitViewProperties() {
            colorStripView?.visibility = View.GONE
            leftDrawableImageView?.visibility = View.GONE
            messageTextView?.visibility = View.GONE
        }
    }
}