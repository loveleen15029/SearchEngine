

import java.util.List;

public class Score {
	public static double calculateNDCG(List<String> realData, List<String> predictionData) {
		if(realData == null)
			return 0;
		double dcg = 0;
		double idcg = calculateIDCG(realData.size());

		if (idcg == 0) {
			return 0;
		}
		for (int i = 0; i < predictionData.size(); i++) {
			String predictedItem = predictionData.get(i);

			if (!realData.contains(predictedItem))
				continue;

			int itemRelevance = 1;
			if (!realData.contains(predictedItem))
				itemRelevance = 0;

			int rank = i + 1;

			dcg += (Math.pow(2, itemRelevance) - 1.0) * (Math.log(2) / Math.log(rank + 1));
		}

		return dcg / idcg;
	}
	public static double calculateIDCG(int n) {
		double idcg = 0;
		int itemRelevance = 1;
		for (int i = 0; i < n; i++){
			idcg += (Math.pow(2, itemRelevance) - 1.0) * ( Math.log(2) / Math.log(i + 2) );
		}
		return idcg;
	}

}