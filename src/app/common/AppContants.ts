export class AppContants {
  private static redirect_uri = 'https://furniture-app-delta.vercel.app/';
  public static URL_GOOGLE =
    'http://localhost:8088/oauth2/authorization/google' +
    '?redirect-uri=' +
    AppContants.redirect_uri;
  public static URL_FACEBOOK =
    'http://localhost:8088/oauth2/authorization/facebook' +
    '?redirect-uri=' +
    AppContants.redirect_uri;
  public static URL_GITHUB =
    'http://localhost:8088/oauth2/authorization/github';
}
