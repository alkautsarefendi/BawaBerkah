
import 'package:bawaberkah/Constants/constants.dart';

class SlideUpdate {
  final UpdateType updateType;
  final SlideDirection direction;
  final double slidePercent;

  SlideUpdate(
      this.direction,
      this.slidePercent,
      this.updateType,
      );
}
