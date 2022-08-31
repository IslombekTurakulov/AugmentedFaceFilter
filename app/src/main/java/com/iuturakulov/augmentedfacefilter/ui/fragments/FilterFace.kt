package com.iuturakulov.augmentedfacefilter.ui.fragments

import android.content.Context
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.ar.core.AugmentedFace
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.AugmentedFaceNode
import com.iuturakulov.augmentedfacefilter.R
import com.iuturakulov.augmentedfacefilter.models.ModelsInfo
import com.iuturakulov.augmentedfacefilter.ui.repository.ModelsRepository
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnSuccess
import timber.log.Timber

class FilterFace(
    augmentedFace: AugmentedFace?,
    private val context: Context,
    modelsRepository: ModelsRepository
) : AugmentedFaceNode(augmentedFace) {

    private var cardNode: Node? = null
    private var textView: TextView? = null
    private var imageView: ImageView? = null
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable

    init {
        suspend {
            modelsRepository.getArchitectureList().suspendOnSuccess {
                data.forEach {
                    modelsList.add(it)
                }
            }.suspendOnError {
                Timber.d("Error ${message()}")
            }
        }
    }

    private val modelsList: ArrayList<ModelsInfo> = ArrayList()

    override fun onActivate() {
        super.onActivate()
        cardNode = Node()
        cardNode?.setParent(this)
        mHandler = Handler()
        ViewRenderable.builder()
            .setView(context, R.layout.card_layout)
            .build()
            .thenAccept { renewable: ViewRenderable ->
                renewable.isShadowCaster = false
                renewable.isShadowReceiver = false
                cardNode?.renderable = renewable
                textView = renewable.view.findViewById(R.id.element_title)
                imageView = renewable.view.findViewById(R.id.element_image)
            }
            .exceptionally { throwable: Throwable? ->
                throw AssertionError(
                    "Could not create ui element",
                    throwable
                )
            }
    }

    override fun onUpdate(frameTime: FrameTime?) {
        super.onUpdate(frameTime)
        augmentedFace?.let { face ->
            val rightForehead = face.getRegionPose(AugmentedFace.RegionType.FOREHEAD_RIGHT)
            val leftForehead = face.getRegionPose(AugmentedFace.RegionType.FOREHEAD_LEFT)
            val center = face.centerPose
            cardNode?.worldPosition = Vector3(
                (leftForehead.tx() + rightForehead.tx()) / 2,
                (leftForehead.ty() + rightForehead.ty()) / 2 + 0.05f, center.tz()
            )
        }
    }

    fun animate() {
        val index = (modelsList.indices).random()
        val rounds = (2..4).random()
        var currentIndex = 0
        var currentRound = 0
        mRunnable = Runnable {
            modifyViews(currentIndex, currentIndex)
            currentIndex++
            if (currentIndex == modelsList.size) {
                currentIndex = 0
                currentRound++
            }
            if (currentRound == rounds) {
                modifyViews(index, currentIndex)
            } else {
                mHandler.postDelayed(
                    mRunnable, // Runnable
                    100 // Delay in milliseconds
                )
            }
        }
        mHandler.postDelayed(
            mRunnable,
            100
        )
    }

    private fun modifyViews(index: Int, currentIndex: Int) {
        textView?.text = modelsList[index].name
        Glide.with(context)
            .load(modelsList[currentIndex].path)
            .into(imageView!!)
    }

    fun refresh() {
        textView?.text = context.getText(R.string.quiz_title)
    }
}