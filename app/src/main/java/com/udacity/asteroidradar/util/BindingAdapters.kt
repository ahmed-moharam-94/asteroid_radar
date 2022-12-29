package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso


// bind status icon in the asteroid item
@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        // set content description for the image view
        imageView.contentDescription = "Asteroid is hazardous"
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        // set content description for the image view
        imageView.contentDescription = "Asteroid is not hazardous"
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        // set content description for the image view
        imageView.contentDescription = "Asteroid is hazardous image"
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        // set content description for the image view
        imageView.contentDescription = "Asteroid is hazardous image"
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

// bind image of the day
@BindingAdapter("imageOfTheDay")
fun bindImageOfTheDay(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    pictureOfDay?.let {
        // check if the media is image
        if (pictureOfDay.mediaType == "image") {
            Picasso.get()
                .load(pictureOfDay.url)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_connection_error)
                .fit()
                .centerCrop()
                .into(imageView)
            imageView.contentDescription = pictureOfDay.title
        }
        else {
            imageView.setImageResource(R.drawable.placeholder_picture_of_day)
            imageView.contentDescription = "Picture of the day did not download"
        }
        }
    }




// status adapter
//@BindingAdapter("marsApiStatus")
//fun bindStatues(statusImageview: ImageView, status:ApiStatus?) {
//    when(status) {
//        ApiStatus.Loading-> {
//            statusImageview.visibility = View.VISIBLE
//            statusImageview.setImageResource(R.drawable.loading_animation)
//        }
//        ApiStatus.ERROR-> {
//            statusImageview.visibility = View.VISIBLE
//            statusImageview.setImageResource(R.drawable.ic_connection_error)
//        }
//        ApiStatus.DONE-> {
//            statusImageview.visibility = View.GONE
//        }
//    }
//}
