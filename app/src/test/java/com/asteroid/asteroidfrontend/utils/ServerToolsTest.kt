package com.asteroid.asteroidfrontend.utils

import com.asteroid.asteroidfrontend.R
import com.asteroid.asteroidfrontend.models.Response
import com.asteroid.asteroidfrontend.models.ServerModel
import io.mockk.*
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.RealmResults
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ServerToolsTest {

    private val VALID_SERVER_NAME = "VALID_SERVER_NAME"
    private val IN_USE_SERVER_NAME = "IN_USE_SERVER_NAME"
    private val VALID_SERVER_ADDRESS = "VALID_SERVER_ADDRESS"
    private val IN_USE_LOCAL_SERVER_ADDRESS = "IN_USE_LOCAL_SERVER_ADDRESS"
    private val IN_USE_PUBLIC_SERVER_ADDRESS = "IN_USE_PUBLIC_SERVER_ADDRESS"
    private val IN_USE_LOCAL_SERVER_ID = 3

    private val IN_USE_SERVERS = listOf(
        ServerModel(IN_USE_SERVER_NAME,"ARB_S_ADD",false),
        ServerModel("ARB_S_NAM",IN_USE_PUBLIC_SERVER_ADDRESS,false),
        ServerModel("OTHER_ARB_S_NAM",IN_USE_LOCAL_SERVER_ADDRESS,true,IN_USE_LOCAL_SERVER_ID)
    )

    private lateinit var mockRealm: Realm

    private fun generateMockQuery(serverList: List<ServerModel>): RealmQuery<ServerModel> {
        val newQueryMock = mockk<RealmQuery<ServerModel>>()
        val stringSlot = slot<String>()
        val boolSlot = slot<Boolean>()
        val intSlot = slot<Int>()
        every { newQueryMock.equalTo("name",capture(stringSlot)) } answers {
            generateMockQuery(serverList.filter { it.name == stringSlot.captured })
        }
        every { newQueryMock.equalTo("address",capture(stringSlot)) } answers {
            generateMockQuery(serverList.filter { it.address == stringSlot.captured })
        }
        every { newQueryMock.equalTo("local",capture(boolSlot)) } answers {
            generateMockQuery(serverList.filter { it.local == boolSlot.captured }) }
        every { newQueryMock.equalTo("wifiNetworkId",capture(intSlot)) } answers {
            generateMockQuery(serverList.filter { it.wifiNetworkId == intSlot.captured }) }
        every { newQueryMock.findAll() } answers {
            val results = mockk<RealmResults<ServerModel>>()
            every { results.isEmpty() } returns serverList.isEmpty()
            results
        }
        return newQueryMock
    }

    @Before
    fun setup() {
        mockRealm = mockk()
        every { mockRealm.where<ServerModel>() } answers { generateMockQuery(IN_USE_SERVERS) }
        val lambdaSlot = slot<Realm.Transaction>()
        every { mockRealm.executeTransaction(capture(lambdaSlot)) } answers {
            val transaction = lambdaSlot.captured
            transaction.execute(mockRealm)
        }
    }

    @Test
    fun addNewServer_freshInput_succeeds() {
        val emptyServerModel = ServerModel()
        val stringSlot = slot<String>()
        every { mockRealm.createObject<ServerModel>(capture(stringSlot)) } answers {
            emptyServerModel.name = stringSlot.captured
            emptyServerModel
        }
        val response = ServerTools.addNewServer(mockRealm,VALID_SERVER_NAME,VALID_SERVER_ADDRESS,false)
        assertTrue("Response indicated failure - success expected", response.success)
        verify { mockRealm.executeTransaction(any()) }
        val failureString = "\t | \t expected ServerModel \t | \t received ServerModel \n"
            .plus("Name \t | \t").plus(VALID_SERVER_NAME).plus(" \t | \t ").plus(emptyServerModel.name).plus("\n")
            .plus("Address \t | \t").plus(VALID_SERVER_ADDRESS).plus(" \t | \t ").plus(emptyServerModel.address).plus("\n")
            .plus("Local \t | \t").plus(false).plus(" \t | \t ").plus(emptyServerModel.local).plus("\n")
            .plus("WifiId \t | \t").plus(null).plus(" \t | \t ").plus(emptyServerModel.wifiNetworkId).plus("\n")
        assertTrue(failureString, emptyServerModel.name == VALID_SERVER_NAME)
        assertTrue(failureString, emptyServerModel.address == VALID_SERVER_ADDRESS)
        assertTrue(failureString, !emptyServerModel.local)
        assertTrue(failureString, emptyServerModel.wifiNetworkId == null)
    }

    @Test
    fun addNewServer_missingName_fails() {
        val response = ServerTools.addNewServer(mockRealm,"",VALID_SERVER_ADDRESS,false)
        assertTrue(Response(  false, R.string.server_name_empty_prompt) == response)
    }

    @Test
    fun addNewServer_missingAddress_fails() {
        val response = ServerTools.addNewServer(mockRealm,VALID_SERVER_NAME,"",false)
        assertTrue(Response(  false, R.string.server_address_empty_prompt) == response)
    }

    @Test
    fun addNewServer_inUseName_fails() {
        val response = ServerTools.addNewServer(mockRealm,IN_USE_SERVER_NAME,VALID_SERVER_ADDRESS,false)
        assertTrue(Response(  false, R.string.server_name_in_use_prompt) == response)
    }

    @Test
    fun addNewServer_inUsePublicAddress_fails() {
        val response = ServerTools.addNewServer(mockRealm,VALID_SERVER_NAME,IN_USE_PUBLIC_SERVER_ADDRESS,false)
        assertTrue(Response(  false, R.string.server_address_in_use_prompt) == response)
    }

    @Test
    fun addNewServer_inUseLocalAddressSameID_fails() {
        val response = ServerTools.addNewServer(mockRealm,VALID_SERVER_NAME,IN_USE_LOCAL_SERVER_ADDRESS,true, IN_USE_LOCAL_SERVER_ID)
        assertTrue(Response(  false, R.string.server_address_in_use_prompt) == response)
    }

    @Test
    fun addNewServer_inUseLocalAddressDifferentID_succeeds() {
        val emptyServerModel = ServerModel()
        val stringSlot = slot<String>()
        every { mockRealm.createObject<ServerModel>(capture(stringSlot)) } answers {
            emptyServerModel.name = stringSlot.captured
            emptyServerModel
        }
        val response = ServerTools.addNewServer(mockRealm,VALID_SERVER_NAME,IN_USE_LOCAL_SERVER_ADDRESS,true, IN_USE_LOCAL_SERVER_ID+1)
        assertTrue(response.success)
        verify { mockRealm.executeTransaction(any()) }
        val failureString = "\t | \t expected ServerModel \t | \t received ServerModel \n"
            .plus("Name \t | \t").plus(VALID_SERVER_NAME).plus(" \t | \t ").plus(emptyServerModel.name).plus("\n")
            .plus("Address \t | \t").plus(IN_USE_LOCAL_SERVER_ADDRESS).plus(" \t | \t ").plus(emptyServerModel.address).plus("\n")
            .plus("Local \t | \t").plus(true).plus(" \t | \t ").plus(emptyServerModel.local).plus("\n")
            .plus("WifiId \t | \t").plus(IN_USE_LOCAL_SERVER_ID+1).plus(" \t | \t ").plus(emptyServerModel.wifiNetworkId).plus("\n")
        assertTrue(failureString, emptyServerModel.name == VALID_SERVER_NAME)
        assertTrue(failureString, emptyServerModel.address == IN_USE_LOCAL_SERVER_ADDRESS)
        assertTrue(failureString, emptyServerModel.local)
        assertTrue(failureString, emptyServerModel.wifiNetworkId == IN_USE_LOCAL_SERVER_ID+1)
    }

}