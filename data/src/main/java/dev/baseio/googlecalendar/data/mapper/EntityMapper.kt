package dev.baseio.googlecalendar.data.mapper

interface EntityMapper<Domain, Data> {
  fun mapToDomain(entity: Data): Domain

  fun mapToData(model: Domain): Data
}
