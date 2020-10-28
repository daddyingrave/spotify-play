package com.github.andreyelagin.spotifyplay.allbums.domain;

public enum DatePrecision {
  YEAR("year"),
  MONTH("month"),
  DAY("day");

  String precision;

  DatePrecision(final String precision) {
    this.precision = precision;
  }
}
