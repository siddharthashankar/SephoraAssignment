package com.sid.sephoraproduct.models

import io.mockk.mockk
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test

class SephoraDataTest{

    @Test
    fun getSephoraDataTest() {
        val attributes = mockk<Attributes>()
        var id: String? = "Sephora"
        var type: String? = "Face mask"
        var image_url: String? = null
        var social_rank: Float = 0f
        var title: String? = "Sep"

        val sephoraData = SephoraData(id, type,image_url = "",social_rank = 1.1f,title ="Sep",attributes = attributes)
        assertEquals(sephoraData.id, id)
        assertEquals(sephoraData.type, type)
        assertEquals(sephoraData.attributes, attributes)
        assertEquals(sephoraData.title, title)


    }
}