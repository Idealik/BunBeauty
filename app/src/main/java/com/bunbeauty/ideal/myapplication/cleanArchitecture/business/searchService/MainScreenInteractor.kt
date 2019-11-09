package com.bunbeauty.ideal.myapplication.cleanArchitecture.business.searchService

import com.bunbeauty.ideal.myapplication.cleanArchitecture.business.FiguringServicePoints
import com.bunbeauty.ideal.myapplication.cleanArchitecture.business.searchService.iSearchService.IMainScreenInteractor
import com.bunbeauty.ideal.myapplication.cleanArchitecture.callback.IServiceSubscriber
import com.bunbeauty.ideal.myapplication.cleanArchitecture.callback.IUserSubscriber
import com.bunbeauty.ideal.myapplication.cleanArchitecture.callback.MainScreenCallback
import com.bunbeauty.ideal.myapplication.cleanArchitecture.models.entity.Service
import com.bunbeauty.ideal.myapplication.cleanArchitecture.models.entity.User
import com.bunbeauty.ideal.myapplication.cleanArchitecture.repositories.ServiceRepository
import com.bunbeauty.ideal.myapplication.cleanArchitecture.repositories.UserRepository
import com.bunbeauty.ideal.myapplication.helpApi.WorkWithTimeApi
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class MainScreenInteractor(val userRepository: UserRepository,
                           val serviceRepository: ServiceRepository) : IMainScreenInteractor,
        IUserSubscriber, IServiceSubscriber, CoroutineScope {


    private lateinit var mainScreenCallback: MainScreenCallback
    var selectedCategory = ""

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    //cache
    private var currentCountOfUsers = 0
    private var cacheMainScreenData = ArrayList<ArrayList<Any>>()
    private var cachePremiumMainScreenData = ArrayList<ArrayList<Any>>()
    private var cacheServiceList = arrayListOf<Service>()
    private var cacheUserList = arrayListOf<User>()

    private var createMainScreenWithCategory = true

    override fun getMainScreenData(mainScreenCallback: MainScreenCallback) {
        this.mainScreenCallback = mainScreenCallback
        if (isFirstEnter(getUserId(), cachedUserIds)) {
            userRepository.getById(getUserId(), this, false)
        } else {
            clearCache()
            userRepository.getById(getUserId(), this, false)
        }
    }

    private fun clearCache() {
        cacheMainScreenData.clear()
        cacheServiceList.clear()
        cacheUserList.clear()
        cachePremiumMainScreenData.clear()
        currentCountOfUsers = 0
    }

    override fun getMainScreenData(category: String, mainScreenCallback: MainScreenCallback) {
        //can add if which will check cache size and if 0 will load from FB
        mainScreenCallback.returnMainScreenData(convertCacheDataToMainScreenData(category,cacheMainScreenData))
    }

    override fun getUsersByCity(city: String) {
        userRepository.getByCity(city, this, isFirstEnter(getUserId(), cachedUserIds))
    }

    override fun getServicesByUserId(id: String) {
        serviceRepository.getServicesByUserId(id, this, true)
    }

    override fun returnUser(user: User) {
        //here we can get out city
        getUsersByCity(user.city)
    }

    override fun returnUsers(users: List<User>) {
        launch {
            setListenerCountOfReturnServices(users.size)
        }

        cacheUserList.addAll(users)

        for (user in users) {
            getServicesByUserId(user.id)
        }
    }

    override fun returnServiceList(serviceList: List<Service>) {
        currentCountOfUsers++
        cacheServiceList.addAll(serviceList)
    }

    override suspend fun setListenerCountOfReturnServices(countOfUsers: Int) {
        while (countOfUsers != currentCountOfUsers) {
            delay(500)
        }

        for (service in cacheServiceList) {
            addToServiceList(service, getUserByService(service))
        }
        cacheMainScreenData = choosePremiumServices(cachePremiumMainScreenData, cacheMainScreenData)
        if (createMainScreenWithCategory) {
            createMainScreenWithCategory = false
            mainScreenCallback.returnMainScreenDataWithCreateCategory(convertCacheDataToMainScreenData(cacheMainScreenData))
        } else {
            mainScreenCallback.returnMainScreenData(convertCacheDataToMainScreenData(cacheMainScreenData))
        }
    }

    override fun convertCacheDataToMainScreenData(cacheMainScreenData: ArrayList<ArrayList<Any>>): ArrayList<ArrayList<Any>> {
        val mainScreenData = ArrayList<ArrayList<Any>>()
        for (i in cacheMainScreenData.indices) {
            //services
            mainScreenData.add(arrayListOf(cacheMainScreenData[i][1], cacheMainScreenData[i][2]))
        }
        return mainScreenData
    }

    override fun convertCacheDataToMainScreenData(category: String, cacheMainScreenData: ArrayList<ArrayList<Any>>): ArrayList<ArrayList<Any>> {
        val mainScreenData = ArrayList<ArrayList<Any>>()
        for (i in cacheMainScreenData.indices) {
            //services 1 , users 2
            if ((cacheMainScreenData[i][1] as Service).category == category)
                mainScreenData.add(arrayListOf(cacheMainScreenData[i][1], cacheMainScreenData[i][2]))
        }
        return mainScreenData
    }

    private fun getUserByService(service: Service): User {
        for (user in cacheUserList) {
            if (service.userId == user.id)
                return user
        }
        return User()
    }

    override fun returnService(service: Service) {
        //log
    }

    //and than we have to get all services by this users
    private fun isFirstEnter(id: String, idList: ArrayList<String>): Boolean {
        if (idList.contains(id)) {
            return false
        }
        idList.add(id)
        return true
    }

    // Добавляем конкретную услугу в список в сообветствии с её коэфициентом
    private fun addToServiceList(service: Service, user: User) {
        val coefficients = HashMap<String, Float>()
        coefficients[Service.CREATION_DATE] = 0.25f
        coefficients[Service.COST] = 0.07f
        coefficients[Service.AVG_RATING] = 0.53f
        coefficients[Service.COUNT_OF_RATES] = 0.15f
        //Проверку на премиум вынести, этот метод не должен делать 2 дейсвтия (можно сразу проверять в выборе рандомных премиум сервисов)
        val isPremium = WorkWithTimeApi.getIsPremium(service.premiumDate)

        if (isPremium) {
            cachePremiumMainScreenData.add(0, arrayListOf(1f, service, user))
        } else {
            val creationDatePoints = FiguringServicePoints.figureCreationDatePoints(service.creationDate, coefficients[Service.CREATION_DATE]!!)
            val costPoints = FiguringServicePoints.figureCostPoints((service.cost).toLong(),
                    serviceRepository.getMaxCost().cost.toLong(),
                    coefficients[Service.COST]!!)

            val ratingPoints = FiguringServicePoints.figureRatingPoints(service.rating, coefficients[Service.AVG_RATING]!!)
            val countOfRatesPoints = FiguringServicePoints.figureCountOfRatesPoints(service.countOfRates,
                    serviceRepository.getMaxCountOfRates().countOfRates,
                    coefficients[Service.COUNT_OF_RATES]!!)

            val points = creationDatePoints + costPoints + ratingPoints + countOfRatesPoints
            sortAddition(arrayListOf(points, service, user))
        }
    }


    private fun sortAddition(serviceData: ArrayList<Any>) {
        for (i in cacheServiceList.indices) {
            if (cacheMainScreenData.size != 0) {
                if (cacheMainScreenData[i][0].toString().toFloat() < (serviceData[0]).toString().toFloat()) {
                    cacheMainScreenData.add(i, serviceData)
                    return
                }
            }
            break
        }
        cacheMainScreenData.add(cacheMainScreenData.size, serviceData)
    }

    override fun getCategories(mainScreenData: ArrayList<ArrayList<Any>>): MutableSet<String> {
        val setOfCategories = mutableSetOf<String>()
        for (i in mainScreenData.indices) {
            setOfCategories.add((mainScreenData[i][0] as Service).category)
        }
        return setOfCategories
    }

    private fun choosePremiumServices(premiumList: ArrayList<ArrayList<Any>>,
                                      cacheMainScreenData: ArrayList<ArrayList<Any>>): ArrayList<ArrayList<Any>> {
        val random = Random()
        val limit = 3

        if (premiumList.size <= limit) {
            cacheMainScreenData.addAll(0, premiumList)
        } else {
            for (i in 0 until limit) {
                var premiumService: ArrayList<Any>
                do {
                    val index = random.nextInt(premiumList.size)
                    premiumService = premiumList[index]
                } while (cacheMainScreenData.contains(premiumService))
                cacheMainScreenData.add(0, premiumService)
            }
        }
        return cacheMainScreenData
    }
    override fun getUserId(): String = FirebaseAuth.getInstance().currentUser!!.uid

    companion object {
        //can be replaced by one var
        val cachedUserIds = arrayListOf<String>()
    }
}