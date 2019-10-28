union () {
  difference () {
    translate ([-2.85, 0, 0]) {
      union () {
        color ([0, 1, 0, ]) {
          hull () {
            translate ([0, 0, 5]) {
              cube ([1.2, 2.9, 0.01], center=true);
            }
            translate ([0, 0, -3]) {
              translate ([0, 0, 0]) {
                cube ([1.2, 2.9, 0.01], center=true);
              }
            }
          }
        }
        color ([0, 1, 0, ]) {
          hull () {
            translate ([5.7, 0, 5]) {
              cube ([1.2, 2.9, 0.01], center=true);
            }
            translate ([0, 0, -3]) {
              translate ([5.7, 0, 0]) {
                cube ([1.2, 2.9, 0.01], center=true);
              }
            }
          }
        }
      }
    }
    translate ([0, 0, 5.5]) {
      rotate ([0.0,-5.000000000000001,0.0]) {
        translate ([0, 0, 75.93375000000005]) {
          scale ([1, 2.1, 1]) {
            sphere ($fn=100, r=77.43375000000005);
          }
        }
      }
    }
  }
  difference () {
    difference () {
      hull () {
        translate ([0, 0, 5.5]) {
          rotate ([0.0,-5.000000000000001,0.0]) {
            minkowski () {
              cube ([15.450000000000003, 8.3, 0.0099], center=true);
              cylinder ($fs=0.1, h=1.0E-4, r=3, center=true);
            }
          }
        }
        minkowski () {
          cube ([25.25, 16.5, 0.0099], center=true);
          cylinder ($fs=0.1, h=1.0E-4, r=0.5, center=true);
        }
      }
      translate ([0, 0, 5.5]) {
        rotate ([0.0,-5.000000000000001,0.0]) {
          translate ([0, 0, 75.93375000000005]) {
            scale ([1, 2.1, 1]) {
              sphere ($fn=100, r=77.43375000000005);
            }
          }
        }
      }
    }
    #union () {
      hull () {
        rotate ([0.0,-5.000000000000001,0.0]) {
          translate ([0, 0, 2.5]) {
            minkowski () {
              cube ([13.950000000000003, 6.800000000000001, 0.0099], center=true);
              cylinder ($fs=0.1, h=1.0E-4, r=3, center=true);
            }
          }
        }
        translate ([0, 0, -0.001]) {
          minkowski () {
            cube ([23.75, 15.0, 0.0099], center=true);
            cylinder ($fs=0.1, h=1.0E-4, r=0.5, center=true);
          }
        }
      }
    }
  }
}
