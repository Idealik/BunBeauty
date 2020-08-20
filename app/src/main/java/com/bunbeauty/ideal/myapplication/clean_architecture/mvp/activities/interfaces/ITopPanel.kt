package com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.interfaces

import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.android.ideal.myapplication.R
import com.bunbeauty.ideal.myapplication.clean_architecture.business.CircularTransformation
import com.bunbeauty.ideal.myapplication.clean_architecture.enums.ButtonTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.part_top_panel.*
import kotlinx.android.synthetic.main.part_top_panel.view.*

interface ITopPanel : IPanel, Toolbar.OnMenuItemClickListener {

    fun initTopPanel(title: String, buttonTask: ButtonTask) {
        initTopPanel(buttonTask)
        setTitle(title)
    }

    fun initTopPanel(buttonTask: ButtonTask) {
        configBackIcon()
        configPanel(buttonTask)
    }

    fun initTopPanel(title: String) {
        configBackIcon()
        configPanel(ButtonTask.NONE)
    }

    fun setTitle(title: String){
        panelContext.topPanel.titleTopPanelText.text = title
    }

    fun initTopPanel(
        title: String,
        buttonTask: ButtonTask,
        photoLink: String
    ) {
        initTopPanel(title, buttonTask)

        val imageView = panelContext.topPanel.avatarTopPanelImage
        setAvatar(photoLink, imageView)
    }

    private fun configBackIcon() {
        if (panelContext.isTaskRoot) {
            panelContext.topPanel.navigationIcon = null
        } else {
            panelContext.topPanel.setNavigationOnClickListener {
                panelContext.onBackPressed()
                panelContext.overridePendingTransition(0, 0)
            }
        }
    }

    private fun configPanel(buttonTask: ButtonTask) {
        when (buttonTask) {
            ButtonTask.NONE -> {
                hideActionIcon()
                hideImageView()
            }
            ButtonTask.EDIT -> {
                configActionIcon(R.drawable.icon_edit)
            }
            ButtonTask.GO_TO_PROFILE -> {
                hideActionIcon()
                panelContext.topPanel.avatarTopPanelImage.setOnClickListener {
                    actionClick()
                }
            }
            ButtonTask.SEARCH -> {
                configActionIcon(R.drawable.icon_search)
            }
            ButtonTask.LOGOUT -> {
                configActionIcon(R.drawable.icon_logout)
            }
        }
    }

    private fun hideActionIcon() {
        panelContext.topPanel.menu.findItem(R.id.navigation_action).isVisible = false
    }

    private fun hideImageView() {
        panelContext.topPanel.avatarTopPanelImage.visibility = View.GONE
    }

    private fun configActionIcon(iconId: Int) {
        panelContext.topPanel.menu.findItem(R.id.navigation_action).icon =
            panelContext.getDrawable(iconId)
        panelContext.topPanel.setOnMenuItemClickListener(this)

        hideImageView()
    }

    private fun setAvatar(photoLink: String, imageView: ImageView) {
        val width = panelContext.resources.getDimensionPixelSize(R.dimen.photo_width)
        val height = panelContext.resources.getDimensionPixelSize(R.dimen.photo_height)
        Picasso.get()
            .load(photoLink)
            .resize(width, height)
            .centerCrop()
            .transform(CircularTransformation())
            .into(imageView)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navigation_action -> {
                actionClick()
                true
            }
            else -> {
                false
            }
        }
    }

    fun actionClick() {}
}