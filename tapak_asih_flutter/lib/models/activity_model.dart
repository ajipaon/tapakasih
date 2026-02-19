/// Activity model for tracking
class ActivityModel {
  final int epochTime;
  final String pageName;
  final String sessionId;

  ActivityModel({
    required this.epochTime,
    required this.pageName,
    required this.sessionId,
  });

  Map<String, dynamic> toJson() {
    return {
      'epochtime': epochTime,
      'pageName': pageName,
      'sessionId': sessionId,
    };
  }

  factory ActivityModel.fromJson(Map<String, dynamic> json) {
    return ActivityModel(
      epochTime: json['epochtime'] as int,
      pageName: json['pageName'] as String,
      sessionId: json['sessionId'] as String,
    );
  }
}