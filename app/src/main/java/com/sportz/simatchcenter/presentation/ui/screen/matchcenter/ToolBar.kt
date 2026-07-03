package com.sportz.simatchcenter.presentation.ui.screen.matchcenter

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sportz.simatchcenter.R


@Composable
fun ToolBar(
    title: String,
    showBack: Boolean = true,
    showShare: Boolean = false,
    shareClick: () -> Unit,
    backClick: () -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.purple_700))
    ) {
        Column(
            horizontalAlignment = Alignment.Start, modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            if (showBack) {
                Image(
                    modifier = Modifier.clickable {
                        backClick()
                    },
                    painter = painterResource(id = R.drawable.ic_back_arrow),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }

        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Text(
                fontWeight = FontWeight.Bold, modifier = Modifier.clickable {
                    Toast.makeText(context, title, Toast.LENGTH_SHORT).show()
                }, text = title, color = Color.White
            )

        }
        Column(
            horizontalAlignment = Alignment.End, modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            if (showShare) {
                Image(
                    modifier = Modifier.clickable {
                        shareClick.invoke()
                    },
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }

        }
    }
}