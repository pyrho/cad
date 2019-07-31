include <BH-Lib/all.scad>
union () {
  difference () {
    difference () {
      union () {
        translate ([0, -26.325, 0]) {
          rotate ([0.0,0.0,180.0]) {
            difference () {
              difference () {
                hull () {
                  translate ([-24.78, -15.399999999999999, 0]) {
                    linear_extrude (height=9.9){
                      circle ($fn=80, r=10);
                    }
                  }
                  translate ([24.78, -15.399999999999999, 0]) {
                    linear_extrude (height=9.9){
                      circle ($fn=80, r=10);
                    }
                  }
                  translate ([24.78, 15.399999999999999, 0]) {
                    linear_extrude (height=9.9){
                      circle ($fn=80, r=10);
                    }
                  }
                  translate ([-24.78, 15.399999999999999, 0]) {
                    linear_extrude (height=9.9){
                      circle ($fn=80, r=10);
                    }
                  }
                }
                #union () {
                  hull () {
                    translate ([-23.83, -14.45, 0]) {
                      linear_extrude (height=8.0){
                        circle ($fn=80, r=10);
                      }
                    }
                    translate ([23.83, -14.45, 0]) {
                      linear_extrude (height=8.0){
                        circle ($fn=80, r=10);
                      }
                    }
                    translate ([23.83, 14.45, 0]) {
                      linear_extrude (height=8.0){
                        circle ($fn=80, r=10);
                      }
                    }
                    translate ([-23.83, 14.45, 0]) {
                      linear_extrude (height=8.0){
                        circle ($fn=80, r=10);
                      }
                    }
                  }
                }
              }
              translate ([0, 24.45, 0]) {
                cube ([72.56, 5, 12.9], center=true);
              }
            }
          }
        }
        hull () {
          difference () {
            translate ([0, -10, 9]) {
              rotate ([10.000000000000002,0.0,0.0]) {
                difference () {
                  translate ([0, 0, 5.25]) {
                    cube ([72.65, 11.6, 10.5], center=true);
                  }
                  translate ([0, 0, 69.45]) {
                    rotate ([90.0,0.0,0.0]) {
                      hull () {
                        reflect () {
                          translate ([11.0, 11.0, 0]) {
                            translate ([-35.375, -69.45, 0]) {
                              torus(5.65, 5.35);
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
            translate ([0, -5, 9]) {
              rotate ([10.000000000000002,0.0,0.0]) {
                cube ([100, 20, 30], center=true);
              }
            }
          }
          difference () {
            translate ([0, -26.325, 0]) {
              rotate ([0.0,0.0,180.0]) {
                difference () {
                  difference () {
                    hull () {
                      translate ([-24.78, -15.399999999999999, 0]) {
                        linear_extrude (height=9.9){
                          circle ($fn=80, r=10);
                        }
                      }
                      translate ([24.78, -15.399999999999999, 0]) {
                        linear_extrude (height=9.9){
                          circle ($fn=80, r=10);
                        }
                      }
                      translate ([24.78, 15.399999999999999, 0]) {
                        linear_extrude (height=9.9){
                          circle ($fn=80, r=10);
                        }
                      }
                      translate ([-24.78, 15.399999999999999, 0]) {
                        linear_extrude (height=9.9){
                          circle ($fn=80, r=10);
                        }
                      }
                    }
                    #union () {
                      hull () {
                        translate ([-23.83, -14.45, 0]) {
                          linear_extrude (height=8.0){
                            circle ($fn=80, r=10);
                          }
                        }
                        translate ([23.83, -14.45, 0]) {
                          linear_extrude (height=8.0){
                            circle ($fn=80, r=10);
                          }
                        }
                        translate ([23.83, 14.45, 0]) {
                          linear_extrude (height=8.0){
                            circle ($fn=80, r=10);
                          }
                        }
                        translate ([-23.83, 14.45, 0]) {
                          linear_extrude (height=8.0){
                            circle ($fn=80, r=10);
                          }
                        }
                      }
                    }
                  }
                  translate ([0, 24.45, 0]) {
                    cube ([72.56, 5, 12.9], center=true);
                  }
                }
              }
            }
            translate ([0, -15, 0]) {
              cube ([100, 50, 30], center=true);
            }
          }
        }
        hull () {
          difference () {
            translate ([0, -26.325, 0]) {
              rotate ([0.0,0.0,180.0]) {
                difference () {
                  difference () {
                    hull () {
                      translate ([-24.78, -15.399999999999999, 0]) {
                        linear_extrude (height=9.9){
                          circle ($fn=80, r=10);
                        }
                      }
                      translate ([24.78, -15.399999999999999, 0]) {
                        linear_extrude (height=9.9){
                          circle ($fn=80, r=10);
                        }
                      }
                      translate ([24.78, 15.399999999999999, 0]) {
                        linear_extrude (height=9.9){
                          circle ($fn=80, r=10);
                        }
                      }
                      translate ([-24.78, 15.399999999999999, 0]) {
                        linear_extrude (height=9.9){
                          circle ($fn=80, r=10);
                        }
                      }
                    }
                    #union () {
                      hull () {
                        translate ([-23.83, -14.45, 0]) {
                          linear_extrude (height=8.0){
                            circle ($fn=80, r=10);
                          }
                        }
                        translate ([23.83, -14.45, 0]) {
                          linear_extrude (height=8.0){
                            circle ($fn=80, r=10);
                          }
                        }
                        translate ([23.83, 14.45, 0]) {
                          linear_extrude (height=8.0){
                            circle ($fn=80, r=10);
                          }
                        }
                        translate ([-23.83, 14.45, 0]) {
                          linear_extrude (height=8.0){
                            circle ($fn=80, r=10);
                          }
                        }
                      }
                    }
                  }
                  translate ([0, 24.45, 0]) {
                    cube ([72.56, 5, 12.9], center=true);
                  }
                }
              }
            }
            translate ([0, -30, 0]) {
              cube ([100, 50, 30], center=true);
            }
          }
          difference () {
            translate ([0, -10, 9]) {
              rotate ([10.000000000000002,0.0,0.0]) {
                difference () {
                  translate ([0, 0, 5.25]) {
                    cube ([72.65, 11.6, 10.5], center=true);
                  }
                  translate ([0, 0, 69.45]) {
                    rotate ([90.0,0.0,0.0]) {
                      hull () {
                        reflect () {
                          translate ([11.0, 11.0, 0]) {
                            translate ([-35.375, -69.45, 0]) {
                              torus(5.65, 5.35);
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
            translate ([0, -15, 10]) {
              rotate ([10.000000000000002,0.0,0.0]) {
                cube ([100, 20, 30], center=true);
              }
            }
          }
        }
        hull () {
          difference () {
            translate ([0, -26.325, 0]) {
              rotate ([0.0,0.0,180.0]) {
                difference () {
                  difference () {
                    hull () {
                      translate ([-24.78, -15.399999999999999, 0]) {
                        linear_extrude (height=9.9){
                          circle ($fn=80, r=10);
                        }
                      }
                      translate ([24.78, -15.399999999999999, 0]) {
                        linear_extrude (height=9.9){
                          circle ($fn=80, r=10);
                        }
                      }
                      translate ([24.78, 15.399999999999999, 0]) {
                        linear_extrude (height=9.9){
                          circle ($fn=80, r=10);
                        }
                      }
                      translate ([-24.78, 15.399999999999999, 0]) {
                        linear_extrude (height=9.9){
                          circle ($fn=80, r=10);
                        }
                      }
                    }
                    #union () {
                      hull () {
                        translate ([-23.83, -14.45, 0]) {
                          linear_extrude (height=8.0){
                            circle ($fn=80, r=10);
                          }
                        }
                        translate ([23.83, -14.45, 0]) {
                          linear_extrude (height=8.0){
                            circle ($fn=80, r=10);
                          }
                        }
                        translate ([23.83, 14.45, 0]) {
                          linear_extrude (height=8.0){
                            circle ($fn=80, r=10);
                          }
                        }
                        translate ([-23.83, 14.45, 0]) {
                          linear_extrude (height=8.0){
                            circle ($fn=80, r=10);
                          }
                        }
                      }
                    }
                  }
                  translate ([0, 24.45, 0]) {
                    cube ([72.56, 5, 12.9], center=true);
                  }
                }
              }
            }
            translate ([-17.5, -30, 0]) {
              cube ([100, 60, 30], center=true);
            }
          }
          intersection () {
            translate ([0, -10, 9]) {
              rotate ([10.000000000000002,0.0,0.0]) {
                difference () {
                  translate ([0, 0, 5.25]) {
                    cube ([72.65, 11.6, 10.5], center=true);
                  }
                  translate ([0, 0, 69.45]) {
                    rotate ([90.0,0.0,0.0]) {
                      hull () {
                        reflect () {
                          translate ([11.0, 11.0, 0]) {
                            translate ([-35.375, -69.45, 0]) {
                              torus(5.65, 5.35);
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
            translate ([41, -10, 9]) {
              rotate ([10.000000000000002,0.0,0.0]) {
                cube ([10, 20, 30], center=true);
              }
            }
          }
        }
        hull () {
          difference () {
            translate ([0, -26.325, 0]) {
              rotate ([0.0,0.0,180.0]) {
                difference () {
                  difference () {
                    hull () {
                      translate ([-24.78, -15.399999999999999, 0]) {
                        linear_extrude (height=9.9){
                          circle ($fn=80, r=10);
                        }
                      }
                      translate ([24.78, -15.399999999999999, 0]) {
                        linear_extrude (height=9.9){
                          circle ($fn=80, r=10);
                        }
                      }
                      translate ([24.78, 15.399999999999999, 0]) {
                        linear_extrude (height=9.9){
                          circle ($fn=80, r=10);
                        }
                      }
                      translate ([-24.78, 15.399999999999999, 0]) {
                        linear_extrude (height=9.9){
                          circle ($fn=80, r=10);
                        }
                      }
                    }
                    #union () {
                      hull () {
                        translate ([-23.83, -14.45, 0]) {
                          linear_extrude (height=8.0){
                            circle ($fn=80, r=10);
                          }
                        }
                        translate ([23.83, -14.45, 0]) {
                          linear_extrude (height=8.0){
                            circle ($fn=80, r=10);
                          }
                        }
                        translate ([23.83, 14.45, 0]) {
                          linear_extrude (height=8.0){
                            circle ($fn=80, r=10);
                          }
                        }
                        translate ([-23.83, 14.45, 0]) {
                          linear_extrude (height=8.0){
                            circle ($fn=80, r=10);
                          }
                        }
                      }
                    }
                  }
                  translate ([0, 24.45, 0]) {
                    cube ([72.56, 5, 12.9], center=true);
                  }
                }
              }
            }
            translate ([17.5, -30, 0]) {
              cube ([100, 60, 30], center=true);
            }
          }
          intersection () {
            translate ([0, -10, 9]) {
              rotate ([10.000000000000002,0.0,0.0]) {
                difference () {
                  translate ([0, 0, 5.25]) {
                    cube ([72.65, 11.6, 10.5], center=true);
                  }
                  translate ([0, 0, 69.45]) {
                    rotate ([90.0,0.0,0.0]) {
                      hull () {
                        reflect () {
                          translate ([11.0, 11.0, 0]) {
                            translate ([-35.375, -69.45, 0]) {
                              torus(5.65, 5.35);
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
            translate ([-41, -10, 9]) {
              rotate ([10.000000000000002,0.0,0.0]) {
                #union () {
                  cube ([10, 20, 30], center=true);
                }
              }
            }
          }
        }
        translate ([0, -10, 9]) {
          rotate ([10.000000000000002,0.0,0.0]) {
            difference () {
              translate ([0, 0, 5.25]) {
                cube ([72.65, 11.6, 10.5], center=true);
              }
              translate ([0, 0, 69.45]) {
                rotate ([90.0,0.0,0.0]) {
                  hull () {
                    reflect () {
                      translate ([11.0, 11.0, 0]) {
                        translate ([-35.375, -69.45, 0]) {
                          torus(5.65, 5.35);
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
      translate ([0, -10, 9]) {
        rotate ([10.000000000000002,0.0,0.0]) {
          translate ([0, 5.6, 10.5]) {
            rotate ([90.0,0.0,0.0]) {
              #union () {
                cylinder (h=11.6, r1=9, r2=6, center=true);
              }
            }
          }
        }
      }
      translate ([0, -8.5, 0]) {
        rotate ([10.000000000000002,0.0,0.0]) {
          #union () {
            cube ([55.75, 7.5, 50], center=true);
          }
        }
      }
      translate ([0, -26.325, 0]) {
        hull () {
          translate ([-23.83, -14.45, 0]) {
            linear_extrude (height=8.0){
              circle ($fn=80, r=10);
            }
          }
          translate ([23.83, -14.45, 0]) {
            linear_extrude (height=8.0){
              circle ($fn=80, r=10);
            }
          }
          translate ([23.83, 14.45, 0]) {
            linear_extrude (height=8.0){
              circle ($fn=80, r=10);
            }
          }
          translate ([-23.83, 14.45, 0]) {
            linear_extrude (height=8.0){
              circle ($fn=80, r=10);
            }
          }
        }
      }
    }
    translate ([28.83, 0, 0]) {
      translate ([-16, 0, 2]) {
        translate ([0, 0, 10]) {
          rotate ([10.000000000000002,0.0,0.0]) {
            rotate ([90.0,0.0,0.0]) {
              #union () {
                cylinder (h=20, r=1, center=true);
              }
            }
          }
        }
      }
      translate ([-12, 0, 2]) {
        translate ([0, 0, 10]) {
          rotate ([10.000000000000002,0.0,0.0]) {
            rotate ([90.0,0.0,0.0]) {
              #union () {
                cylinder (h=20, r=1, center=true);
              }
            }
          }
        }
      }
      translate ([-8, 0, 2]) {
        translate ([0, 0, 10]) {
          rotate ([10.000000000000002,0.0,0.0]) {
            rotate ([90.0,0.0,0.0]) {
              #union () {
                cylinder (h=20, r=1, center=true);
              }
            }
          }
        }
      }
      translate ([-4, 0, 2]) {
        translate ([0, 0, 10]) {
          rotate ([10.000000000000002,0.0,0.0]) {
            rotate ([90.0,0.0,0.0]) {
              #union () {
                cylinder (h=20, r=1, center=true);
              }
            }
          }
        }
      }
    }
    translate ([-9, 0, 0]) {
      translate ([-16, 0, 2]) {
        translate ([0, 0, 10]) {
          rotate ([10.000000000000002,0.0,0.0]) {
            rotate ([90.0,0.0,0.0]) {
              #union () {
                cylinder (h=20, r=1, center=true);
              }
            }
          }
        }
      }
      translate ([-12, 0, 2]) {
        translate ([0, 0, 10]) {
          rotate ([10.000000000000002,0.0,0.0]) {
            rotate ([90.0,0.0,0.0]) {
              #union () {
                cylinder (h=20, r=1, center=true);
              }
            }
          }
        }
      }
      translate ([-8, 0, 2]) {
        translate ([0, 0, 10]) {
          rotate ([10.000000000000002,0.0,0.0]) {
            rotate ([90.0,0.0,0.0]) {
              #union () {
                cylinder (h=20, r=1, center=true);
              }
            }
          }
        }
      }
      translate ([-4, 0, 2]) {
        translate ([0, 0, 10]) {
          rotate ([10.000000000000002,0.0,0.0]) {
            rotate ([90.0,0.0,0.0]) {
              #union () {
                cylinder (h=20, r=1, center=true);
              }
            }
          }
        }
      }
    }
  }
}
$fn = 80;
