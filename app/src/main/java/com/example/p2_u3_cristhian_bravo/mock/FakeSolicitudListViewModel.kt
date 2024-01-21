package com.example.p2_u3_cristhian_bravo.mock

import com.example.p2_u3_cristhian_bravo.ui.SolicitudListViewModel
import android.content.Context

class FakeSolicitudListViewModel(fakeDao: FakeSolicitudDao, context: Context?) : SolicitudListViewModel(fakeDao, context) {
    override fun getLocation() {
    }
}
