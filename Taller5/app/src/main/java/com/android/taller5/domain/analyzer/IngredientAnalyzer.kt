package com.android.taller5.domain.analyzer

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class IngredientAnalyzer(
    private val onIngredientsDetected: (List<String>) -> Unit
) : ImageAnalysis.Analyzer {

    private val labeler = ImageLabeling.getClient(
        ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.5f) // Bajamos a 0.5 para que detecte más fácil en emuladores
            .build()
    )

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            
            labeler.process(image)
                .addOnSuccessListener { labels ->
                    val detected = labels.map { it.text.lowercase() }
                    if (detected.isNotEmpty()) {
                        onIngredientsDetected(detected)
                    }
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}
