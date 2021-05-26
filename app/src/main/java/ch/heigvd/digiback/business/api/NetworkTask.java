package ch.heigvd.digiback.business.api;

public class NetworkTask extends BaseTask {
    private final String baseUrl = "https://infomaldedos.ch/wp-json/wp/v2";
    private final iOnDataFetched listener; //listener in fragment that shows and hides ProgressBar

    public NetworkTask(iOnDataFetched onDataFetchedListener) {
        this.listener = onDataFetchedListener;
    }
    public NetworkTask() {
        this.listener = null;
    }

    @Override
    public Object call() throws Exception {
        Object result = null;
        /*result = someNetworkFunction();//some network request for example
        URL myUrl = new URL(baseUrl);
        HttpsURLConnection conn = (HttpsURLConnection)myUrl.openConnection();
        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String inputLine;

        while ((inputLine = br.readLine()) != null) {
            System.out.println(inputLine);
        }

        br.close();*/
        return null; // result;
    }

    @Override
    public void setUiForLoading() {
        if (listener != null) {
            listener.showProgressBar();
        }
    }

    @Override
    public void setDataAfterLoading(Object result) {
        if (listener != null) {
            listener.setDataInPageWithResult(result);
            listener.hideProgressBar();
        }
    }
}
