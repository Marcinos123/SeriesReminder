package com.example.marci.seriesreminder.ui.common

import com.example.marci.seriesreminder.model.pojo.seasons.Episode
import com.example.marci.seriesreminder.model.realm.SerieRealm
import com.example.marci.seriesreminder.repositories.SeriesRepository
import com.example.marci.seriesreminder.ui.series_watchlist.viewmodel.SubscribedSerieViewModel
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by marci on 2018-02-23.
 */
class GetSubscribedSeriesUseCase @Inject constructor(
    private val seriesRepository: SeriesRepository
) {

  fun get(ids: List<Int>): Single<List<SubscribedSerieViewModel>> {
    return Single.fromCallable {
      seriesRepository.get(SerieRealm::class) {
        if (!ids.isEmpty()) {
          `in`("id", ids.toTypedArray()).findAll().map {
            SubscribedSerieViewModel(
                id = it.id,
                title = it.name,
                originCountry = it.originCountry?.toList(),
                voteCount = it.voteCount,
                originalLanguage = it.originalLanguage,
                voteAverage = it.voteAverage,
                overview = it.overview,
                photoUrl = it.posterPath,
                episodes = it.episodes?.map { episodeRealm ->
                  Episode(
                      id = episodeRealm.id,
                      name = episodeRealm.name,
                      overview = episodeRealm.overview,
                      airDate = episodeRealm.airDate,
                      episodeNumber = episodeRealm.episodeNumber,
                      seasonNumber = episodeRealm.seasonNumber,
                      stillPath = episodeRealm.stillPath,
                      voteAverage = episodeRealm.voteAverage,
                      voteCount = episodeRealm.voteCount
                  )
                }?.toList() ?: emptyList()
            )
          }.toMutableList()
        } else {
          return@get emptyList<SubscribedSerieViewModel>()
        }
      }
    }
  }
}