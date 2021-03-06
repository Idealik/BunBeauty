package com.android.ideal.myapplication

import com.bunbeauty.ideal.myapplication.clean_architecture.data.repositories.ServiceRepository
import com.bunbeauty.ideal.myapplication.clean_architecture.data.repositories.UserRepository
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class MainScreenUnitTests {

    @Mock
    internal lateinit var serviceRepository: ServiceRepository
    @Mock
    internal lateinit var userRepository: UserRepository

 /*   private val mainScreenUserInteractor: MainScreenUserInteractor
        get() = MainScreenUserInteractor(userRepository)

    @Test
    fun convertCacheDataToMainScreenData() {
        val cacheData = ArrayList<ArrayList<Any>>()
        val service = Service()
        val user = User()
        cacheData.add(getDataList(1, user, service))
        //MS data which we are waiting
        val mainScreenData = ArrayList<ArrayList<Any>>()
        mainScreenData.add(arrayListOf(cacheData[0][1], cacheData[0][2]))

        assertEquals(mainScreenUserInteractor.convertCacheDataToMainScreenData(cacheData), mainScreenData)
    }

    @Test
    fun convertCacheDataToMainScreenDataWithCategory() {
        val cacheData = ArrayList<ArrayList<Any>>()
        val service = Service()
        val user = User()
        service.category = "маникюр"
        cacheData.add(getDataList(2, user, service))
        //MS data which we are waiting
        val mainScreenData = ArrayList<ArrayList<Any>>()
        mainScreenData.add(arrayListOf(cacheData[0][1], cacheData[0][2]))
        assertEquals(mainScreenUserInteractor.convertCacheDataToMainScreenData("маникюр", cacheData), mainScreenData)
    }

    @Test
    fun convertCacheDataToMainScreenDataWithWrongCategory() {
        val cacheData = ArrayList<ArrayList<Any>>()
        val service = Service()
        val user = User()
        service.category = "маникюр"
        cacheData.add(getDataList(1, user, service))
        //MS data which we are waiting
        val mainScreenData = ArrayList<ArrayList<Any>>()
        assertEquals(mainScreenUserInteractor.convertCacheDataToMainScreenData("педикюр", cacheData), mainScreenData)
    }

    @Test
    fun convertCacheDataToMainScreenDataWithWrongTags() {
        val cacheData = ArrayList<ArrayList<Any>>()
        val service = Service()
        val user = User()
        val tag = Tag()
        tag.tag = "Бровничи"
        val selectedTagsArray = arrayListOf("Бровнич")
        service.tags.add(tag)
        cacheData.add(getDataList(1, user, service))
        //MS data which we are waiting
        val mainScreenData = ArrayList<ArrayList<Any>>()
        mainScreenData.add(arrayListOf(cacheData[0][1], cacheData[0][2]))
        assertEquals(mainScreenUserInteractor.convertCacheDataToMainScreenData(selectedTagsArray, cacheData), mainScreenData)
    }

    @Test
    fun whenUserIdEqualsServiceOwnerId() {
        val cacheUsers = ArrayList<User>()
        val user1 = User()
        user1.id = "1"
        val user2 = User()
        user2.id = "2"
        val service = Service()
        service.userId = "1"
        cacheUsers.add(user1)
        cacheUsers.add(user2)

        assertEquals(mainScreenUserInteractor.getUserByService(cacheUsers, service), user1)
    }

    @Test
    fun getCategoriesFromMsData() {
        val category1 = "реснички"
        val category2 = "бровушки"

        val categories = mutableSetOf<String>()
        categories.add(category1)
        categories.add(category2)

        val mainScreenData = ArrayList<ArrayList<Any>>()
        val service = Service()
        val service2 = Service()
        val service3 = Service()
        val user = User()
        service.category = category1
        service2.category = category2
        service3.category = category2

        mainScreenData.add(arrayListOf(service, user))
        mainScreenData.add(arrayListOf(service2, user))
        mainScreenData.add(arrayListOf(service3, user))

        assertEquals(mainScreenUserInteractor.getCategories(mainScreenData), categories)
    }

    @Test
    fun isFirstEnter() {
        val cacheUsers = ArrayList<String>()
        val user = "1"
        val user2 = "2"
        cacheUsers.add(user)
        assertEquals(mainScreenUserInteractor.isFirstEnter(user2, cacheUsers), true)
    }

    @Test
    fun notIsFirstEnter() {
        val cacheUsers = ArrayList<String>()
        val user = "1"
        cacheUsers.add(user)
        assertEquals(mainScreenUserInteractor.isFirstEnter(user, cacheUsers), false)
    }

    //choosePremiumServices

    private fun getDataList(points: Int, user: User, service: Service): ArrayList<Any> {
        val data = ArrayList<Any>()
        data.add(points)
        data.add(service)
        data.add(user)
        return data
    }
*/
}

