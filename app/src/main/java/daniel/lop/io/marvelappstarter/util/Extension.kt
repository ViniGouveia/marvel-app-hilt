package daniel.lop.io.marvelappstarter.util

import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(requireContext(), message, duration).show()
}

fun View.show() {
    visibility = VISIBLE
}

fun View.hide() {
    visibility = INVISIBLE
}

fun String.limitDescription(characters: Int): String {
    if (this.length > characters) {
        val firstCharacter = 0
        return "${this.substring(firstCharacter, characters)}..."
    }
    return this
}

fun loadImage(
    imageView: ImageView,
    path: String,
    extension: String
) = Glide.with(imageView.context).load("$path.$extension").into(imageView)
