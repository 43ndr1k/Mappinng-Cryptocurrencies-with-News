export class Constants {

  private static get BASE_URL(): string {
    return 'http://localhost:8080/';
  }

  public static get CURRENCY_URL(): string {
    return this.BASE_URL + 'assets/country-currency';
  }

  public static get CRYPTO_CURRENCY_URL(): string {
    return this.BASE_URL + 'assets/crypto-currency';
  }

  public static get CRYPTO_WORLD_MAP_DATA(): string {
    return this.BASE_URL + 'assets/crypto-currency-sort-by-country';
  }

  public static get CRYPTO_CURRENCY_NEWS_URL(): string {
    return this.BASE_URL + 'assets/website';
  }

  public static get CRYPTO_CURRENCY_NEWS_BY_DATE_URL(): string {
    return this.BASE_URL + 'assets/websites-filtered-by-date';
  }
}
