package seriesreminder.repositories

import io.realm.RealmObject
import io.realm.RealmQuery
import timber.log.Timber
import kotlin.reflect.KClass

/**
 ** Created by marci on 2018-02-20.
 */
open class RealmRepository(private val realmManager: RealmManager) {

  fun copyOrUpdate(realmObject: RealmObject?) {
    val realm = realmManager.getInstance()
    try {
      realm.beginTransaction()
      if (realmObject != null) {
        realm.copyToRealmOrUpdate(realmObject)
      }
      realm.commitTransaction()
    } catch (error: IllegalArgumentException) {
      Timber.e(error, "Unsuccessfully data save operation")
    } finally {
      realm.close()
    }
  }

  fun <RO : RealmObject, R> get(realmClass: KClass<RO>, query: RealmQuery<RO>.() -> R): R? {
    val realm = realmManager.getInstance()
    try {
      return realm.where(realmClass.java).query()
    } finally {
      realm.close()
    }
  }

  fun clear() {
    val realm = realmManager.getInstance()
    try {
      realm.beginTransaction()
      realm.deleteAll()
      realm.commitTransaction()
    } finally {
      realm.close()
    }
  }
}
