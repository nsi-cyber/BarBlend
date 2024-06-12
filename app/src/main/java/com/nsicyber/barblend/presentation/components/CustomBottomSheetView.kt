package com.nsicyber.barblend.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nsicyber.barblend.R
import com.nsicyber.barblend.presentation.detail.BottomSheetState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheetView(
    bottomSheetState: BottomSheetState,
    suggestionValue: String?,
    onValueChange: (text: String) -> Unit,
    onButtonClicked: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
    ) {
        if (bottomSheetState == BottomSheetState.OnFavoriteMessage) {
            Text(
                text = stringResource(id = R.string.bottom_sheet_favorite_success_message),
                style = BarBlendTextStyles.body,
            )
        }
        if (bottomSheetState == BottomSheetState.OnRemoveMessage) {
            Text(
                text = stringResource(id = R.string.bottom_sheet_remove_success_message),
                style = BarBlendTextStyles.body,
            )
        }
        if (bottomSheetState == BottomSheetState.OnError) {
            Text(
                text = stringResource(id = R.string.bottom_sheet_error_message),
                style = BarBlendTextStyles.body,
            )
        }
        if (bottomSheetState == BottomSheetState.OnInput) {
            Text(
                text = stringResource(id = R.string.bottom_sheet_text),
                style = BarBlendTextStyles.body,
            )
        }

        if (bottomSheetState == BottomSheetState.OnInput) {
            TextField(
                placeholder = { Text(text = stringResource(id = R.string.bottom_sheet_placeholder)) },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(color = Color.White),
                value = suggestionValue.orEmpty(),
                onValueChange = { value ->
                    onValueChange(value)
                },
                colors =
                    TextFieldDefaults.textFieldColors(
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        containerColor = Color.White,
                        disabledTextColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                    ),
            )
            Box(
                modifier =
                    Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { onButtonClicked() },
                contentAlignment = Alignment.Center,
            ) {
                Text(text = stringResource(id = R.string.bottom_sheet_button))
            }
        }
    }
}
