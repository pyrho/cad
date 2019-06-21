union () {
  difference () {
    hull () {
      cube ([17.5, 16.6, 0.01], center=true);
      translate ([0, 0, 3.5]) {
        cube ([14.3, 13, 0.01], center=true);
      }
    }
    hull () {
      translate ([0, 0, -1]) {
        cube ([16.0, 15.100000000000001, 0.01], center=true);
      }
      translate ([0, 0, 2.0]) {
        cube ([12.8, 11.5, 0.01], center=true);
      }
    }
  }
  translate ([-2.85, 0, 0.5]) {
    union () {
      cube ([1.2, 3, 3], center=true);
      translate ([5.7, 0, 0]) {
        cube ([1.2, 3, 3], center=true);
      }
    }
  }
}
