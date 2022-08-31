package com.iuturakulov.augmentedfacefilter.ui.activity

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.ArCoreApk
import com.google.ar.core.AugmentedFace
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.rendering.Renderable
import com.iuturakulov.augmentedfacefilter.R
import com.iuturakulov.augmentedfacefilter.databinding.ActivityMainBinding
import com.iuturakulov.augmentedfacefilter.ui.fragments.FaceArFragment
import com.iuturakulov.augmentedfacefilter.ui.fragments.FilterFace
import com.iuturakulov.augmentedfacefilter.ui.repository.ModelsRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        const val MIN_OPENGL_VERSION = 3.0
    }

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var modelsRepository: ModelsRepository
    var faceNodeMap = HashMap<AugmentedFace, FilterFace>()
    var refresh: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (checkIsSupportedDeviceOrFinish()) {
            val sceneView = (binding.faceFragment as FaceArFragment).arSceneView
            sceneView.cameraStreamRenderPriority = Renderable.RENDER_PRIORITY_FIRST
            sceneView.scene.addOnUpdateListener {
                sceneView.session
                    ?.getAllTrackables(AugmentedFace::class.java)?.let {
                        for (item in it) {
                            if (!faceNodeMap.containsKey(item)) {
                                val faceNode = FilterFace(item, this, modelsRepository)
                                faceNode.setParent(sceneView.scene)
                                faceNodeMap[item] = faceNode
                            }
                        }
                        val inter = faceNodeMap.entries.iterator()
                        while (inter.hasNext()) {
                            val entry = inter.next()
                            val face = entry.key
                            if (face.trackingState == TrackingState.STOPPED) {
                                val faceNode = entry.value
                                faceNode.setParent(null)
                                inter.remove()
                            }
                        }
                    }
            }

            binding.buttonRefresh.setOnClickListener {
                if (refresh) {
                    refresh()
                } else {
                    startQuiz()
                }
                refresh = !refresh
            }
        }
    }

    private fun startQuiz() {
        for (face in faceNodeMap.values) {
            face.animate()
        }
    }

    private fun refresh() {
        for (face in faceNodeMap.values) {
            face.refresh()
        }
    }

    private fun checkIsSupportedDeviceOrFinish(): Boolean {
        if (ArCoreApk.getInstance()
                .checkAvailability(this) == ArCoreApk.Availability.UNSUPPORTED_DEVICE_NOT_CAPABLE
        ) {
            Toast.makeText(this, "Augmented Faces requires ARCore", Toast.LENGTH_LONG).show()
            finish()
            return false
        }
        val openGlVersionString = (getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager)
            ?.deviceConfigurationInfo
            ?.glEsVersion
        openGlVersionString?.let { s ->
            if (java.lang.Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
                Toast.makeText(this, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show()
                finish()
                return false
            }
        }
        return true
    }
}